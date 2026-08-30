package br.com.devmribeiro.taskapi.types;

public enum TaskPriority {
	LOW,
	MEDIUM,
	HIGH;
	
	public int getId() {
		return ordinal() + 1;
	}
}