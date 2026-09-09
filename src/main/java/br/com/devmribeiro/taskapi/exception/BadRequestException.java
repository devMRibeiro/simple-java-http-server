package br.com.devmribeiro.taskapi.exception;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = -7494425280366546143L;
	
	public BadRequestException(String message) {
		super(message);
	}
}