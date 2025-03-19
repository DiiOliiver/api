package br.com.contatos.api.dto.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ContactPropertiesDTO {
	private String email;
	private String firstname;
	private String lastname;
	private String phone;
	@SerializedName("hs_lead_status")
	private String leadStatus;
	private Date createdate;
	private Date lastmodifieddate;
}
