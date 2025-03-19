package br.com.contatos.api.controller.auth;

import br.com.contatos.api.dto.LoginRequestDTO;
import br.com.contatos.api.dto.ResponseDTO;
import br.com.contatos.api.entities.User;
import br.com.contatos.api.repository.UserRepository;
import br.com.contatos.api.service.OAuthService;
import br.com.contatos.api.service.RequestHubspotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import static br.com.contatos.api.constants.ExceptionMessages.BAD_REQUEST;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.PathRest.*;
import static br.com.contatos.api.constants.Messages.NEW_ACCESS_REQUEST;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_AUTH_NAME;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_AUTH_DESCRIPTION;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = API + VERSION_1 + AUTH)
@Tag(name = SWAGGER_AUTH_NAME, description = SWAGGER_AUTH_DESCRIPTION)
public class AuthController implements AuthControllerIn {
	private final OAuthService oAuthService;
	private final RequestHubspotService requestHubspotService;
	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public AuthController(
		OAuthService oAuthService,
		RequestHubspotService requestHubspotService,
		JwtEncoder jwtEncoder,
		UserRepository userRepository,
		BCryptPasswordEncoder passwordEncoder
	) {
		this.oAuthService = oAuthService;
		this.requestHubspotService = requestHubspotService;
		this.jwtEncoder = jwtEncoder;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Object login(LoginRequestDTO loginRequest) {
		try {
			Optional<User> userOptional = this.userRepository.findByUsername(loginRequest.getUsername());
			if (userOptional.isEmpty() || !userOptional.get().isLoginCorrect(loginRequest, passwordEncoder)) {
				return ResponseEntity.badRequest().body(BAD_REQUEST);
			}
			ResponseDTO responseDTO = this.oAuthService.login(userOptional.get(), jwtEncoder);
			return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO.getData());
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Object authorize(HttpServletResponse response) {
		try {
			ResponseDTO responseDTO = this.oAuthService.generateAuthorizationUrl();
			return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO.getData());
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Object callback(String code) {
		try {
			if (code.isBlank()) {
				return ResponseEntity.badRequest().body(BAD_REQUEST + NEW_ACCESS_REQUEST);
			}
			ResponseDTO responseDTO = this.requestHubspotService.exchangeCodeForTokens(code);
			return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO.getData());
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}
}
