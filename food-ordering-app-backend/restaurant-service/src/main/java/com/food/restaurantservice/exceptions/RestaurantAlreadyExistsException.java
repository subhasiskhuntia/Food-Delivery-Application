package com.food.restaurantservice.exceptions;

public class RestaurantAlreadyExistsException extends Exception{
	public RestaurantAlreadyExistsException(String msg){
		super(msg);
	}
}
