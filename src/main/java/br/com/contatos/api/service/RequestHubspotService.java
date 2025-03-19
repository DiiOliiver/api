package br.com.contatos.api.service;

import br.com.contatos.api.dto.AccessTokenBodyDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.entities.Contact;
import br.com.contatos.api.service.session.SessionTokenHubspotService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import java.util.HashMap;
import java.util.Map;
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
	private final WebClient webClient;

	public RequestHubspotService(
		@Value("${spring.security.oauth2.client.registration.hubspot.client-id}") String clientId,
		@Value("${spring.security.oauth2.client.registration.hubspot.client-secret}") String clientSecret,
		@Value("${spring.security.oauth2.client.registration.hubspot.redirect-uri}") String redirectUri,
		WebClient.Builder webClientBuilder
	) {
		HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.webClient = webClientBuilder.baseUrl(HUBAPI)
			.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}

	public ResponseDTO exchangeCodeForTokens(String code) {
		try {
			Object session = SessionTokenHubspotService.getSession();
			if (session != null) {
				Gson gson = new GsonBuilder().create();
				AccessTokenBodyDTO tokenResponseDTO = gson.fromJson(session.toString(), AccessTokenBodyDTO.class);
				if (tokenResponseDTO.getCode() != null && tokenResponseDTO.getCode().matches(code)) {
					return new ResponseDTO(HttpStatus.OK, tokenResponseDTO.getData());
				}
				SessionTokenHubspotService.dropSession();
			}
			return new ResponseDTO(HttpStatus.OK, this.enchangeTokens(code));
		} catch (Exception exception) {
			SessionTokenHubspotService.dropSession();
			return new ResponseDTO(HttpStatus.BAD_REQUEST, NEW_ACCESS_REQUEST);
		}
	}

	private Object enchangeTokens(String code) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "authorization_code");
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("redirect_uri", redirectUri);
		formData.add("code", code);

		String response = this.webClient.post()
			.uri(OAUTH + VERSION_1 + TOKEN)
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.body(BodyInserters.fromFormData(formData))
			.retrieve()
			.bodyToMono(String.class)
			.doOnNext(body -> log.info("Response: {}", body))
			.block();

		log.info("Final Response: {}", response);

		SessionTokenHubspotService.saveSession(response, formData.getFirst("code"));

		return response;
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
