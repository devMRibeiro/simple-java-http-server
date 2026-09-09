package br.com.devmribeiro.taskapi.dto;

import java.time.LocalDateTime;

import br.com.devmribeiro.taskapi.types.TaskPriority;
import br.com.devmribeiro.taskapi.types.TaskStatus;

public record TaskResponseList(
		String title,
		String description,
		TaskStatus status,
		TaskPriority priority,
		LocalDateTime dueDate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {
}