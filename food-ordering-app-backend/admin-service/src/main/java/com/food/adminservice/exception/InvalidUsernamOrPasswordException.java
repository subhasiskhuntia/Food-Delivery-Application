package com.food.adminservice.exception;

public class InvalidUsernamOrPasswordException extends Exception {
	public InvalidUsernamOrPasswordException() {
		super();
	}

	public InvalidUsernamOrPasswordException(String message) {
		super(message);
	}
}
