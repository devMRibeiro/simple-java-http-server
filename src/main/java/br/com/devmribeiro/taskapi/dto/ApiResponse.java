package br.com.devmribeiro.taskapi.dto;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ApiResponse {
	private final int status;
	private final String message;
	private final OffsetDateTime timestamp;

	public ApiResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = OffsetDateTime.now();
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getTimestamp() {
		return timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
}