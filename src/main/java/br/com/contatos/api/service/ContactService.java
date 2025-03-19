package br.com.contatos.api.service;

import br.com.contatos.api.dto.ContactDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.entities.Contact;
import br.com.contatos.api.entities.User;
import br.com.contatos.api.repository.ContactRepository;
import br.com.contatos.api.repository.UserRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;
import static br.com.contatos.api.constants.ExceptionMessages.BAD_REQUEST;
import static br.com.contatos.api.constants.ExceptionMessages.TOKEN_HUBSPOT_INVALID;
import static br.com.contatos.api.constants.ExceptionMessages.USER_EXISTS;

@Service
public class ContactService {

	private final ContactRepository contactRepository;

	private final UserRepository userRepository;
	private final RequestHubspotService requestHubspotService;

	private final HikariDataSource dataSource;

	@Autowired
	public ContactService(
		ContactRepository contactRepository,
		UserRepository userRepository,
		RequestHubspotService requestHubspotService,
		HikariDataSource dataSource
	) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
		this.requestHubspotService = requestHubspotService;
		this.dataSource = dataSource;
	}

	@Transactional
	public ResponseDTO createContact(ContactDTO contactDTO, HttpServletRequest request) throws Exception {
		try (Connection connection = this.dataSource.getConnection()) {
			if (this.contactRepository.existsByEmail(contactDTO.getEmail())) {
				return new ResponseDTO(HttpStatus.BAD_REQUEST, USER_EXISTS);
			}
			connection.setAutoCommit(false);
			String tokenHubspot = request.getHeader("Token-Hubspot");
			if (tokenHubspot == null || tokenHubspot.isEmpty()) {
				return new ResponseDTO(HttpStatus.BAD_REQUEST, TOKEN_HUBSPOT_INVALID);
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() instanceof Jwt jwt && !jwt.getSubject().isEmpty()) {
				UUID userId = UUID.fromString(jwt.getSubject());
				Optional<User> userOptional = this.userRepository.findById(userId);
				if (userOptional.isPresent()) {
					Contact contactEntitie = Contact.newContact(contactDTO, userOptional.get());
					Contact contactSave = this.contactRepository.save(contactEntitie);
					if (contactSave != null) {
						connection.commit();
						this.requestHubspotService.createContactInHubSpot(contactSave, tokenHubspot);
						return new ResponseDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso.");
					}
				}
			}
			connection.rollback();
			return new ResponseDTO(HttpStatus.BAD_REQUEST, BAD_REQUEST);
		} catch (Exception exception) {
			throw exception;
		}
	}
}
