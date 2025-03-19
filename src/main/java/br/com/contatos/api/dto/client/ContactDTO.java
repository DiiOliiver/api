package br.com.contatos.api.dto.client;

import br.com.contatos.api.entities.Contact;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
	private String email;
	private String firstname;
	private String lastname;
	private String phone;
	private String lifecyclestage;
	@SerializedName("hs_lead_status")
	private String hsleadstatus;

	public static ContactDTO from(Contact contact) {
		ContactDTO responseDTO = new ContactDTO();
		responseDTO.setEmail(contact.getEmail());
		responseDTO.setFirstname(contact.getFirstname());
		responseDTO.setLastname(contact.getLastname());
		responseDTO.setPhone(contact.getPhone());
		responseDTO.setLifecyclestage(contact.getLifecyclestage());
		responseDTO.setHsleadstatus(contact.getLeadStatus());
		return responseDTO;
	}
}
