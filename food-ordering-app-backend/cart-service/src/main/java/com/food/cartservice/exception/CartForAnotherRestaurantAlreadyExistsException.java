package com.food.cartservice.exception;

public class CartForAnotherRestaurantAlreadyExistsException extends Exception {
	public CartForAnotherRestaurantAlreadyExistsException(String msg) {
		super(msg);
	}

	public CartForAnotherRestaurantAlreadyExistsException() {
	}
}
