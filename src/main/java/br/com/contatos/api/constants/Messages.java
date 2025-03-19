package br.com.contatos.api.constants;

import static br.com.contatos.api.constants.PathRest.*;

public class Messages {
	private Messages() {
		throw new IllegalStateException(Messages.UTILITY_CLASS);
	}

	public static final String UTILITY_CLASS = "Classe de utilidade.";
	public static final String AUTH_LOGIN_OK = "Logado com sucesso.";
	public static final String AUTH_INSTALL_OK = "Inicializando nova instalação do HubSpot.";
	public static final String AUTH_CALLBACK_OK = "Trocando o código de autenticação por tokens.";
	public static final String NEW_ACCESS_REQUEST = " Solicite novo acesso: " + API + VERSION_1 + AUTH + INSTALL;
	public static final String CREATE_REGISTER = "Registro cadastrado com sucesso.";
	public static final String CREATED_USER_ADMIN = "Usuário administrador criado.";
}
