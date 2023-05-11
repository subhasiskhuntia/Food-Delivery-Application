package com.food.adminservice.exception;

public class ResourceNotFoundException extends Exception {
	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
