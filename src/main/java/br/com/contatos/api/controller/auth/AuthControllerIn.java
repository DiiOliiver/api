package br.com.contatos.api.controller.auth;

import br.com.contatos.api.dto.LoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static br.com.contatos.api.constants.ExceptionMessages.BAD_REQUEST;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.Messages.*;
import static br.com.contatos.api.constants.PathRest.*;
import static br.com.contatos.api.constants.SwaggerMessages.*;

public interface AuthControllerIn {
	@PostMapping(LOGIN)
	@Operation(summary = SWAGGER_AUTH_LOGIN_SUMMARY)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AUTH_LOGIN_OK),
		@ApiResponse(responseCode = "400", description = BAD_REQUEST),
		@ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
	})
	Object login(@RequestBody LoginRequestDTO loginRequest);

	@GetMapping(INSTALL)
	@Operation(summary = SWAGGER_AUTH_INSTALL_SUMMARY)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AUTH_INSTALL_OK),
		@ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
	})
	Object authorize(HttpServletResponse response);

	@GetMapping(CALLBACK)
	@Operation(summary = SWAGGER_AUTH_CALLBACK_SUMMARY)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AUTH_CALLBACK_OK),
		@ApiResponse(responseCode = "400", description = BAD_REQUEST),
		@ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
	})
	Object callback(String code);
}
