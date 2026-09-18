package br.com.devmribeiro.taskapi.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.devmribeiro.taskapi.dto.TaskCreateDTO;
import br.com.devmribeiro.taskapi.dto.TaskUpdateDTO;
import br.com.devmribeiro.taskapi.http.server.HttpResponse;
import br.com.devmribeiro.taskapi.http.server.HttpStatus;
import br.com.devmribeiro.taskapi.http.server.Json;
import br.com.devmribeiro.taskapi.model.Task;
import br.com.devmribeiro.taskapi.service.TaskService;

public class TaskController {
	
	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	public HttpResponse list() {

	    List<Task> tasks = taskService.list();

	    String json = Json.toJson(tasks);

	    return new HttpResponse(HttpStatus.OK, Map.of("Content-Type", "application/json"), json);
	}
	
	public HttpResponse findById(UUID taskId) {

		Task response = taskService.findById(taskId);

	    String json = Json.toJson(response);

	    return new HttpResponse(HttpStatus.OK, Map.of("Content-Type", "application/json"), json);
	}
	
	public HttpResponse create(InputStream body) {
		taskService.create(Json.fromJson(body, TaskCreateDTO.class));
		return new HttpResponse(HttpStatus.CREATED, Map.of("Content-Type", "application/json"), null);
	}

	public HttpResponse update(UUID taskId, InputStream body) {
		taskService.update(taskId, Json.fromJson(body, TaskUpdateDTO.class));
		return new HttpResponse(HttpStatus.OK, Map.of("Content-Type", "application/json"), null);
	}
	
	public HttpResponse delete(UUID taskId) {
		taskService.delete(taskId);
		return new HttpResponse(HttpStatus.OK, Map.of("Content-Type", "application/json"), null);
	}
}