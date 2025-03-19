package br.com.contatos.api.service;

import br.com.contatos.api.dto.AccessTokenBodyDTO;
import br.com.contatos.api.dto.AccessTokenResponseDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.dto.client.ContactPropertiesResponseDTO;
import br.com.contatos.api.dto.client.EventWebhookDTO;
import br.com.contatos.api.entities.Contact;
import br.com.contatos.api.repository.ContactRepository;
import br.com.contatos.api.service.session.SessionTokenHubspotService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import br.com.contatos.api.repository.UserRepository;
import java.util.*;
import static br.com.contatos.api.constants.Messages.NEW_ACCESS_REQUEST;
import static br.com.contatos.api.constants.ClientRest.HUBAPI;
import static br.com.contatos.api.constants.ClientRest.OAUTH;
import static br.com.contatos.api.constants.ClientRest.VERSION_1;
import static br.com.contatos.api.constants.ClientRest.TOKEN;
import static br.com.contatos.api.constants.ClientRest.VERSION_3;
import static br.com.contatos.api.constants.ClientRest.CRM;
import static br.com.contatos.api.constants.ClientRest.OBJECTS;
import static br.com.contatos.api.constants.ClientRest.CONTACTS;

@Slf4j
@Service
public class RequestHubspotService {

	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private final String refresh_token;
	private final UserRepository userRepository;
	private final ContactRepository contactRepository;
	private final WebClient webClient;

	public RequestHubspotService(
		@Value("${spring.security.oauth2.client.registration.hubspot.client-id}") String clientId,
		@Value("${spring.security.oauth2.client.registration.hubspot.client-secret}") String clientSecret,
		@Value("${spring.security.oauth2.client.registration.hubspot.redirect-uri}") String redirectUri,
		@Value("${spring.security.oauth2.client.registration.hubspot.refresh-token}") String refresh_token,
		UserRepository userRepository,
		ContactRepository contactRepository,
		WebClient.Builder webClientBuilder
	) {
		HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.refresh_token = refresh_token;
		this.userRepository = userRepository;
		this.contactRepository = contactRepository;
		this.webClient = webClientBuilder.baseUrl(HUBAPI)
			.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}

	public ResponseDTO exchangeCodeForTokens(String code) {
		try {
			Gson gson = new GsonBuilder().create();
			Object session = SessionTokenHubspotService.getSession();
			if (session != null) {
				AccessTokenBodyDTO tokenResponseDTO = gson.fromJson(session.toString(), AccessTokenBodyDTO.class);
				if (tokenResponseDTO.getCode() != null && tokenResponseDTO.getCode().matches(code)) {
					return new ResponseDTO(HttpStatus.OK, tokenResponseDTO.getData());
				}
				SessionTokenHubspotService.dropSession();
			}
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("grant_type", "authorization_code");
			formData.add("client_id", clientId);
			formData.add("client_secret", clientSecret);
			formData.add("redirect_uri", redirectUri);
			formData.add("code", code);
			Object response = this.enchangeTokens(formData);
			SessionTokenHubspotService.saveSession(response, code);
			return new ResponseDTO(HttpStatus.OK, response);
		} catch (Exception exception) {
			SessionTokenHubspotService.dropSession();
			return new ResponseDTO(HttpStatus.BAD_REQUEST, NEW_ACCESS_REQUEST);
		}
	}

	private String enchangeTokens(MultiValueMap<String, String> formData) {
		var response = this.webClient.post()
			.uri(OAUTH + VERSION_1 + TOKEN)
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.body(BodyInserters.fromFormData(formData))
			.retrieve()
			.onStatus(HttpStatusCode::isError, res ->
				res.bodyToMono(String.class).flatMap(errorBody -> {
					log.error("Error response from HubSpot: {}", errorBody);
					return Mono.error(new RuntimeException("Failed to refresh token: " + errorBody));
				})
			)
			.bodyToMono(String.class)
			.doOnError(throwable -> log.error("Error exchanging tokens: {}", throwable.getMessage()))
			.block();
		return response;
	}

	public void createRegisterWebhook(List<EventWebhookDTO> payloads) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

		formData.add("grant_type", "refresh_token");
		formData.add("client_id", this.clientId);
		formData.add("client_secret", this.clientSecret);
		formData.add("refresh_token", this.refresh_token);

		Gson gson = new GsonBuilder().create();
		String token = gson.fromJson(this.enchangeTokens(formData), AccessTokenResponseDTO.class).getAccess_token();

		for (EventWebhookDTO payload: payloads) {
			String url = CRM + VERSION_3 + OBJECTS + CONTACTS + "/" + payload.getObjectId();
			String response = this.webClient.get()
				.uri(uriBuilder -> uriBuilder
					.path(url)
					.queryParam("properties", "email,firstname,lastname,phone,hs_lead_status")
					.build())
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.bodyToMono(String.class)
				.block();

			ContactPropertiesResponseDTO responseFormat = gson.fromJson(response, ContactPropertiesResponseDTO.class);
			Contact contact = Contact.newContact(responseFormat.getProperties());
			contact.setUser(this.userRepository.findByUsername("admin").get());
			this.contactRepository.save(contact);
		}
	}

	public void createContactInHubSpot(Contact contact, String token) {
		Map<String, Object> contactData = new HashMap<>();
		contactData.put("properties", Map.of(
			"email", contact.getEmail(),
			"firstname", contact.getFirstname(),
			"lastname", contact.getLastname(),
			"phone", contact.getPhone(),
			"lifecyclestage", contact.getLifecyclestage()
		));

		String response = this.webClient.post()
			.uri(CRM + VERSION_3 + OBJECTS + CONTACTS)
			.header("Authorization", "Bearer " + token)
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.bodyValue(contactData)
			.retrieve()
			.bodyToMono(String.class)
			.doOnNext(body -> log.info("Response: {}", body))
			.block();

		log.info("Final Response: {}", response);
	}
}
