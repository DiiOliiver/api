package br.com.contatos.api.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPropertiesResponseDTO {
	private String id;
	private ContactPropertiesDTO properties;
}
