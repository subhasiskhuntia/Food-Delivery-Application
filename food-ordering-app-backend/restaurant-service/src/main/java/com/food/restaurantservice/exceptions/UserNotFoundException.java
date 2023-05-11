package com.food.restaurantservice.exceptions;

public class UserNotFoundException extends Exception{
	public UserNotFoundException(String msg){
		super(msg);
	}
}
