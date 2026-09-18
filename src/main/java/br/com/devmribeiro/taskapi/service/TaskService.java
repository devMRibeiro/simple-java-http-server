package br.com.devmribeiro.taskapi.service;

import java.util.List;
import java.util.UUID;

import br.com.devmribeiro.taskapi.dto.TaskCreateDTO;
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
		
		if (createDTO == null || isEmptyAny(createDTO.title(), createDTO.description(), createDTO.priority(), createDTO.dueDate()))
			throw new BadRequestException("Preencha os todos os campos corretamente");
		
		taskRepository.create(createDTO);
	}

	public void update(UUID taskId, TaskUpdateDTO updateDTO) {
		
		if (updateDTO == null || isEmptyAny(updateDTO.title(), updateDTO.description(), updateDTO.status(), updateDTO.priority(), updateDTO.dueDate(), taskId))
			throw new BadRequestException("Preencha os todos os campos corretamente");
		
		taskRepository.update(taskId, updateDTO);
	}
	
	public void delete(UUID taskId) {
		
		if (isEmpty(taskId))
			throw new BadRequestException("Informe taskId");
			
		taskRepository.delete(taskId);
	}
	
	public List<Task> list() {
		return taskRepository.list(null);
	}
	
	public Task findById(UUID id) {
		
		if (isEmpty(id))
			throw new BadRequestException("Informe taskId");
		
		List<Task> tasks = taskRepository.list(id);

		if (!isEmpty(tasks) && tasks.size() > 0)
			return tasks.get(0);
		
		return null;
	}
	
	private boolean isEmpty(Object value) {
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