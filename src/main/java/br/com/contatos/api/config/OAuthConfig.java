package br.com.contatos.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class OAuthConfig {

	@Value("${spring.security.oauth2.client.registration.hubspot.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.hubspot.client-secret}")
	private String secretId;

	@Value("${spring.security.oauth2.client.registration.hubspot.authorization-uri}")
	private String authorizationUri;

	@Value("${spring.security.oauth2.client.registration.hubspot.token-uri}")
	private String tokenUri;

	@Value("${spring.security.oauth2.client.registration.hubspot.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.registration.hubspot.scope}")
	private String scope;

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(clientRegistration());
	}

	private ClientRegistration clientRegistration() {
		return ClientRegistration.withRegistrationId("hubspot")
			.clientId(clientId)
			.clientSecret(secretId)
			.authorizationUri(authorizationUri)
			.tokenUri(tokenUri)
			.redirectUri(redirectUri)
			.scope(scope)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.build();
	}
}
