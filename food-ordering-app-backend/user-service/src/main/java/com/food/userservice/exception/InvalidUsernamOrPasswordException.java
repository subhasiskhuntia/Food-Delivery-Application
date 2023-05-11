package com.food.userservice.exception;

public class InvalidUsernamOrPasswordException extends Exception{
	
	public InvalidUsernamOrPasswordException(String msg) {
		super(msg);
	}

}
