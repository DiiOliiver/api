package br.com.contatos.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
	private String username;
	private String password;
}
