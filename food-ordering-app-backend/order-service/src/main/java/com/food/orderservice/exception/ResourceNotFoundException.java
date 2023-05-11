package com.food.orderservice.exception;

public class ResourceNotFoundException extends Exception {
	public ResourceNotFoundException(String msg) {
		super(msg);
	}

	public ResourceNotFoundException() {
	}
}
