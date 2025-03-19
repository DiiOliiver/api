package br.com.contatos.api.entities.enums;

public enum LeadStatusEnum {
	NEW("new"),
	OPEN("open");

	String value;

	LeadStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
