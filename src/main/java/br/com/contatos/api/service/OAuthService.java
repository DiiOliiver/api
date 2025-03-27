package br.com.contatos.api.service;

import br.com.contatos.api.dto.LoginRequestDTO;
import br.com.contatos.api.dto.LoginResponseDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.entities.Role;
import br.com.contatos.api.entities.User;
import br.com.contatos.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import static br.com.contatos.api.constants.ExceptionMessages.BAD_REQUEST;

@Service
public class OAuthService {

	private String clientId;

	private String clientSecret;

	private String redirectUri;

	private String authorizationUri;

	private final String scope;

	private final RequestHubspotService requestHubspotService;
	private final UserRepository userRepository;
	private final JwtEncoder jwtEncoder;
	private final BCryptPasswordEncoder passwordEncoder;

	OAuthService(
		@Value("${spring.security.oauth2.client.registration.hubspot.client-id}") String clientId,
		@Value("${spring.security.oauth2.client.registration.hubspot.client-secret}") String clientSecret,
		@Value("${spring.security.oauth2.client.registration.hubspot.redirect-uri}") String redirectUri,
		@Value("${spring.security.oauth2.client.registration.hubspot.authorization-uri}") String authorizationUri,
		@Value("${spring.security.oauth2.client.registration.hubspot.scope}") String scope,
		RequestHubspotService requestHubspotService,
		UserRepository userRepository,
		JwtEncoder jwtEncoder,
		BCryptPasswordEncoder passwordEncoder
	) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.authorizationUri = authorizationUri;
		this.scope = scope;
		this.requestHubspotService = requestHubspotService;
		this.userRepository = userRepository;
		this.jwtEncoder = jwtEncoder;
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseDTO login(LoginRequestDTO loginRequest) {
		Optional<User> userOptional = this.userRepository.findByUsername(loginRequest.getUsername());
		if (userOptional.isEmpty() || !userOptional.get().isLoginCorrect(loginRequest, passwordEncoder)) {
			return new ResponseDTO(HttpStatus.BAD_REQUEST, BAD_REQUEST);
		}

		User user = userOptional.get();

		var now = Instant.now();
		var expiresIn = 300L;

		var scopes = user.getRoles()
			.stream()
			.map(Role::getName)
			.collect(Collectors.joining(" "));

		var claims = JwtClaimsSet.builder()
			.issuer("api-contatos")
			.subject(user.getId().toString())
			.issuedAt(now)
			.expiresAt(now.plusSeconds(expiresIn))
			.claim("scope", scopes)
			.build();

		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return new ResponseDTO(HttpStatus.CREATED, new LoginResponseDTO(jwtValue, expiresIn));
	}

	public ResponseDTO generateAuthorizationUrl() {
		String url = String.format(
			"%s?client_id=%s&redirect_uri=%s&scope=%s",
			authorizationUri, clientId, redirectUri, scope
		);
		return new ResponseDTO(HttpStatus.OK, url);
	}
}
