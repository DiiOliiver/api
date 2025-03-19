package br.com.contatos.api.controller.webhook;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

	@PostMapping("/contact-created")
	public String contactCreatedWebhook(@RequestBody String payload) {
		System.out.println(payload);
		return "Em progresso...";
	}
}
