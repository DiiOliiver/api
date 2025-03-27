package br.com.contatos.api.service.session;

import br.com.contatos.api.dto.AccessTokenResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SessionTokenHubspotService {
	public static void saveSession(Object data, String code) {
		HashMap<String, Object> register = new HashMap<>();
		register.put("code", code);
		register.put("data", data);

		Gson gson = new GsonBuilder().create();
		AccessTokenResponseDTO tokenResponseDTO = gson.fromJson(data.toString(), AccessTokenResponseDTO.class);

		var session = SessionContextHolderService.getSession();
		session.setMaxInactiveInterval(tokenResponseDTO.getExpires_in());
		session.setAttribute("token_hubspot", register);
	}

	public static Object getSession() {
		return SessionContextHolderService.getSession().getAttribute("token_hubspot");
	}

	public static boolean hasSession() {
		return getSession() != null;
	}

	public static void dropSession() {
		SessionContextHolderService.getSession().removeAttribute("token_hubspot");
	}
}
