package br.com.contatos.api.constants;

public class ExceptionMessages {
	private ExceptionMessages() {
		throw new IllegalStateException(Messages.UTILITY_CLASS);
	}

	public static final String BAD_REQUEST = "Dados inválidos na requisição.";
	public static final String INTERNAL_SERVER_ERROR =
		"Ocorreu um problema inesperado, tente novamente em instantes!";

	public static final String TOKEN_HUBSPOT_INVALID = "Token invalido ou expirado. " + Messages.NEW_ACCESS_REQUEST;

	public static final String USER_EXISTS = "Este usuário está cadastrado, realize o registro de um novo.";
}
