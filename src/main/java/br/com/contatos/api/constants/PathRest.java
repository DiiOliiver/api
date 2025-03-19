package br.com.contatos.api.constants;

public class PathRest {
	private PathRest() {
		throw new IllegalStateException(Messages.UTILITY_CLASS);
	}

	public static final String API = "/api/";
	public static final String VERSION_1 = "v1/";
	public static final String AUTH = "auth";
	public static final String LOGIN = "/login";
	public static final String INSTALL = "/install";
	public static final String CALLBACK = "/callback";
	public static final String CONTACTS = "/contacts";
}
