package br.com.devmribeiro.taskapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.devmribeiro.taskapi.types.TaskPriority;
import br.com.devmribeiro.taskapi.types.TaskStatus;

public record TaskUpdateDTO(
		UUID id,
		String title,
		String description,
		TaskStatus status,
		TaskPriority priority,
		LocalDateTime dueDate,
		UUID userId
) {
}