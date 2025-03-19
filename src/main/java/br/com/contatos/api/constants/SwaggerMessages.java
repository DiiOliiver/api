package br.com.contatos.api.constants;

public class SwaggerMessages {
	private SwaggerMessages() {
		throw new IllegalStateException(Messages.UTILITY_CLASS);
	}
	public static final String SWAGGER_AUTH_NAME = "Autenticação";
	public static final String SWAGGER_AUTH_DESCRIPTION = "Acessos";
	public static final String SWAGGER_AUTH_LOGIN_SUMMARY = "Logando no sistema";
	public static final String SWAGGER_AUTH_INSTALL_SUMMARY = "Nova instalação do HubSpot";
	public static final String SWAGGER_AUTH_CALLBACK_SUMMARY =
		"O usuário foi solicitado a instalar a integração, trocando o código de autenticação por tokens.";
	public static final String SWAGGER_CONTACTS_NAME = "Contatos";
	public static final String SWAGGER_CONTACTS_DESCRIPTION = "Endpoints para registros e listagem de contatos";
	public static final String SWAGGER_CONTACTS_CREATE_SUMMARY = "Logando no sistema";

	public static final String SWAGGER_WEBHOOKS_NAME = "Webhook";
	public static final String SWAGGER_WEBHOOKS_DESCRIPTION = "A HubSpot enviará uma carga útil JSON para essa URL com detalhes sobre os eventos quando eles forem disparados.";
	public static final String SWAGGER_WEBHOOKS_SUMMARY = "Oberva o evento de cadastro dos contatos no HubSpot.";
}
