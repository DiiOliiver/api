package br.com.contatos.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDTO {
	private HttpStatus httpStatus;
	private Object data;

	public ResponseDTO(HttpStatus httpStatus, Object data) {
		this.httpStatus = httpStatus;
		this.data = data;
	}
}
