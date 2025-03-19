package br.com.contatos.api.controller.contacts;

import br.com.contatos.api.dto.ContactDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import static br.com.contatos.api.constants.ExceptionMessages.BAD_REQUEST;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.Messages.CREATE_REGISTER;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_CONTACTS_CREATE_SUMMARY;

public interface ContractControllerIn {

	@PostMapping
	@Operation(summary = SWAGGER_CONTACTS_CREATE_SUMMARY)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = CREATE_REGISTER),
		@ApiResponse(responseCode = "400", description = BAD_REQUEST),
		@ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
	})
	Object create(@RequestBody ContactDTO contactDTO, HttpServletRequest request) throws Exception;
}
