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
}
