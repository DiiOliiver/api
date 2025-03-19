package br.com.contatos.api.dto;

import lombok.Data;

@Data
public class AccessTokenResponseDTO {
	private String token_type;
	private String refresh_token;
	private String access_token;
	private Integer expires_in;
}
