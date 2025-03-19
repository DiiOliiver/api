package br.com.contatos.api.entities;

import br.com.contatos.api.dto.ContactDTO;
import br.com.contatos.api.dto.client.ContactPropertiesDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_contacts")
public class Contact implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstname;
	private String lastname;
	private String email;
	private String phone;

	@Column(name = "life_cycle_stage")
	private String lifecyclestage;

	@Column(name = "hs_lead_status")
	private String leadStatus;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
	private User user;

	@JsonIgnore
	@CreationTimestamp
	protected Date createdAt;

	@JsonIgnore
	@UpdateTimestamp
	protected Date updatedAt;

	public static Contact newContact(ContactDTO dto, User user) {
		Contact newContact = new Contact();
		newContact.setEmail(dto.getEmail());
		newContact.setFirstname(dto.getFirstName());
		newContact.setLastname(dto.getLastName());
		newContact.setPhone(dto.getPhone());
		newContact.setLifecyclestage(dto.getLifeCycleStage());
		newContact.setLeadStatus(dto.getLeadStatus());
		newContact.setUser(user);
		return newContact;
	}

	public static Contact newContact(ContactPropertiesDTO dto) {
		Contact newContact = new Contact();
		newContact.setEmail(dto.getEmail());
		newContact.setFirstname(dto.getFirstname());
		newContact.setLastname(dto.getLastname());
		newContact.setPhone(dto.getPhone());
		newContact.setLeadStatus(dto.getLeadStatus());
		return newContact;
	}
}
