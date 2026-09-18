package br.com.devmribeiro.taskapi.exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;

import br.com.devmribeiro.taskapi.dto.ApiResponse;
import br.com.devmribeiro.taskapi.http.server.HttpResponse;
import br.com.devmribeiro.taskapi.http.server.HttpStatus;
import br.com.devmribeiro.taskapi.http.server.Json;

public class GlobalExceptionHandler {
	
	public void handle(Exception exception, HttpExchange exchange) throws IOException {

		if (exception instanceof BadRequestException) {
			response(exchange, new HttpResponse(HttpStatus.BAD_REQUEST, null, exception.getMessage()));
			return;
		}

		exception.printStackTrace();
		response(exchange, new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, "Unexpected error"));
	}
	
	private void response(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
		byte[] body = Json.toJson(new ApiResponse(httpResponse.getStatus().getCode(), httpResponse.getBody())).getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(httpResponse.getStatus().getCode(), body.length);

        try (var output = exchange.getResponseBody()) {
            output.write(body);
        }
	}
}