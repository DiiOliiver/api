package br.com.contatos.api.controller.contacts;

import br.com.contatos.api.dto.ContactDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.PathRest.*;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_CONTACTS_DESCRIPTION;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_CONTACTS_NAME;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = API + VERSION_1 + CONTACTS)
@Tag(name = SWAGGER_CONTACTS_NAME, description = SWAGGER_CONTACTS_DESCRIPTION)
public class ContactController implements ContractControllerIn {

	private final ContactService contactService;

	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@Override
	public Object create(ContactDTO contactDTO, HttpServletRequest request) throws Exception {
		try {
			ResponseDTO responseDTO = contactService.createContact(contactDTO, request);
			return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO.getData());
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}
}