package br.com.devmribeiro.taskapi.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.devmribeiro.taskapi.types.TaskPriority;
import br.com.devmribeiro.taskapi.types.TaskStatus;

public record Task(
		UUID id,
		String title,
		String description,
		TaskStatus status,
		TaskPriority priority,
		LocalDateTime dueDate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		UUID userId
) {
}