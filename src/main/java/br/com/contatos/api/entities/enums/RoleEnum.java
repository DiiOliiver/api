package br.com.contatos.api.entities.enums;

public enum RoleEnum {
	ADMIN(1L),
	BASIC(2L);

	long roleId;

	RoleEnum(long roleId) {
		this.roleId = roleId;
	}

	public long getRoleId() {
		return roleId;
	}
}
