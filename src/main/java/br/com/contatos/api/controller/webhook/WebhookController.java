package br.com.contatos.api.controller.webhook;

import br.com.contatos.api.dto.client.EventWebhookDTO;
import br.com.contatos.api.service.RequestHubspotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static br.com.contatos.api.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static br.com.contatos.api.constants.PathRest.API;
import static br.com.contatos.api.constants.PathRest.VERSION_1;
import static br.com.contatos.api.constants.PathRest.WEBHOOKS;
import static br.com.contatos.api.constants.SwaggerMessages.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(API + VERSION_1 + WEBHOOKS)
@Tag(name = SWAGGER_WEBHOOKS_NAME, description = SWAGGER_WEBHOOKS_DESCRIPTION)
public class WebhookController implements WebhookControllerIn {

	private final RequestHubspotService requestHubspotService;

	public WebhookController(RequestHubspotService requestHubspotService) {
		this.requestHubspotService = requestHubspotService;
	}

	@Override
	public ResponseEntity<String> contactCreatedWebhook(List<EventWebhookDTO> payload) {
		try {
			this.requestHubspotService.createRegisterWebhook(payload);
			return ResponseEntity.noContent().build();
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}
}
