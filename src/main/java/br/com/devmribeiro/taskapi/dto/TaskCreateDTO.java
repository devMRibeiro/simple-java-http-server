package br.com.devmribeiro.taskapi.dto;

import java.time.LocalDateTime;

import br.com.devmribeiro.taskapi.types.TaskPriority;

public record TaskCreateDTO(
		String title,
		String description,
		TaskPriority priority,
		LocalDateTime dueDate
) {
}