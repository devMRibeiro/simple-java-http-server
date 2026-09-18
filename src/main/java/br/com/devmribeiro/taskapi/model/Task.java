package br.com.devmribeiro.taskapi.model;

import java.util.UUID;

import br.com.devmribeiro.taskapi.types.TaskPriority;
import br.com.devmribeiro.taskapi.types.TaskStatus;

public record Task(
		UUID id,
		String title,
		String description,
		TaskStatus status,
		TaskPriority priority,
		String dueDate,
		String createdAt,
		String updatedAt
) {
}