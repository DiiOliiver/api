package br.com.contatos.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenBodyDTO {
	private String code;
	private AccessTokenResponseDTO data;
}
