package br.com.contatos.api.constants;

public class ClientRest {
	private ClientRest() {
		throw new IllegalStateException(Messages.UTILITY_CLASS);
	}

	public static final String HUBAPI = "https://api.hubapi.com";
	public static final String OAUTH = "/oauth";
	public static final String VERSION_1 = "/v1";
	public static final String VERSION_3 = "/v3";
	public static final String TOKEN = "/token";
	public static final String CRM = "/crm";
	public static final String OBJECTS = "/objects";
	public static final String CONTACTS = "/contacts";
}
