package br.com.contatos.api.dto;

import br.com.contatos.api.entities.enums.LeadStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String lifeCycleStage;
	private String leadStatus = LeadStatusEnum.NEW.getValue();
}
