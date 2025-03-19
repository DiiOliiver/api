package br.com.contatos.api.controller.webhook;

import br.com.contatos.api.dto.client.EventWebhookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.PathRest.CONTACT_CREATED;
import static br.com.contatos.api.constants.SwaggerMessages.SWAGGER_WEBHOOKS_SUMMARY;

public interface WebhookControllerIn {
	@PostMapping(CONTACT_CREATED)
	@Operation(summary = SWAGGER_WEBHOOKS_SUMMARY)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204"),
		@ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
	})
	ResponseEntity<String> contactCreatedWebhook(@RequestBody List<EventWebhookDTO> payload);
}
