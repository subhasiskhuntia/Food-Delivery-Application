package com.food.cartservice.exception;

public class ResourceNotFoundException extends Exception {
	public ResourceNotFoundException(String msg) {
		super(msg);
	}

	public ResourceNotFoundException() {
	}
}
