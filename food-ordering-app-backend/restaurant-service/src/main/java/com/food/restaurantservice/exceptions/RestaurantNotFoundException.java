package com.food.restaurantservice.exceptions;

public class RestaurantNotFoundException extends Exception{
	public RestaurantNotFoundException(String msg){
		super(msg);
	}
}
