package br.com.devmribeiro.taskapi.http.router;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;

import br.com.devmribeiro.taskapi.controller.TaskController;
import br.com.devmribeiro.taskapi.http.server.HttpMethod;
import br.com.devmribeiro.taskapi.http.server.HttpResponse;
import br.com.devmribeiro.taskapi.http.server.HttpStatus;

public class Router {

	private final TaskController taskController;
	
	public Router(TaskController taskController) {
		this.taskController = taskController;
	}

	public HttpResponse handle(HttpExchange exchange) throws IOException {

		String method = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();

		if (method.equals(HttpMethod.GET.name()) && path.equals("/tasks"))
			return taskController.list();

		if (method.equals(HttpMethod.GET.name()) && path.startsWith("/tasks/")) {

			String id = path.substring("/tasks/".length());

			return taskController.findById(UUID.fromString(id));
		}

		if (method.equals(HttpMethod.POST.name()) && path.equals("/tasks"))
			return taskController.create(exchange.getRequestBody());

		if (method.equals(HttpMethod.PUT.name()) && path.startsWith("/tasks/")) {
			UUID taskId = UUID.fromString(path.substring("/tasks/".length()));

			return taskController.update(taskId, exchange.getRequestBody());
		}

		if (method.equals(HttpMethod.DELETE.name()) && path.startsWith("/tasks/")) {
			UUID taskId = UUID.fromString(path.substring("/tasks/".length()));

			return taskController.delete(taskId);
		}

		return sendNotFound();
	}

	private HttpResponse sendNotFound() {
        return new HttpResponse(HttpStatus.NOT_FOUND, Map.of("Content-Type", "text/plain"), "Route not found");
    }
}