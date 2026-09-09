package br.com.devmribeiro.taskapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import br.com.devmribeiro.taskapi.dto.TaskCreateDTO;
import br.com.devmribeiro.taskapi.dto.TaskResponseList;
import br.com.devmribeiro.taskapi.dto.TaskUpdateDTO;
import br.com.devmribeiro.taskapi.exception.BadRequestException;
import br.com.devmribeiro.taskapi.model.Task;
import br.com.devmribeiro.taskapi.repository.TaskRepository;

public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	public void create(TaskCreateDTO createDTO) {
		
		if (createDTO == null || isEmptyAny(createDTO.title(), createDTO.description(), createDTO.status(), createDTO.priority(), createDTO.dueDate(), createDTO.userId()))
			throw new BadRequestException("Preencha os todos os campos corretamente");
		
		taskRepository.create(createDTO);
	}

	public void update(TaskUpdateDTO updateDTO) {
		
		if (updateDTO == null || isEmptyAny(updateDTO.title(), updateDTO.description(), updateDTO.status(), updateDTO.priority(), updateDTO.dueDate(), updateDTO.userId(), updateDTO.id()))
			throw new BadRequestException("Preencha os todos os campos corretamente");
		
		taskRepository.update(updateDTO);
	}
	
	public void delete(UUID taskId, UUID userId) {
		
		if (isEmptyAny(taskId, userId))
			throw new BadRequestException("Informe taskId e userId");
			
		taskRepository.delete(taskId, userId);
	}
	
	public List<TaskResponseList> list(UUID userId) {
		
		if (userId == null)
			throw new BadRequestException("Informe userId");
		
		List<Task> tasks = taskRepository.list(userId);
		
		if (tasks != null && !tasks.isEmpty()) {
			List<TaskResponseList> response = new ArrayList<TaskResponseList>(tasks.size());
			
			for (Task t : tasks) {
				response.add(new TaskResponseList(
							t.title(),
							t.description(),
							t.status(),
							t.priority(),
							t.dueDate(),
							t.createdAt(),
							t.updatedAt()
				));
			}
			return response;
		}
		return Collections.emptyList();
	}
	
	private static boolean isEmpty(Object value) {
		return (value == null || value instanceof String && ((String) value).isBlank());
	}
	
	private boolean isEmptyAny(Object ... values) {
		
		if (values == null)
			return true;
		
		for (Object value : values)
			if (isEmpty(value))
				return true;
		
		return false;
	}
}