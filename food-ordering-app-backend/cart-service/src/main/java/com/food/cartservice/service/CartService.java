package com.food.cartservice.service;

import java.util.List;

import com.food.cartservice.dto.CartDto;
import com.food.cartservice.exception.CartForAnotherRestaurantAlreadyExistsException;
import com.food.cartservice.exception.ResourceNotFoundException;

public interface CartService {

	CartDto addCartItem(CartDto cartDto) throws ResourceNotFoundException,CartForAnotherRestaurantAlreadyExistsException;

	CartDto updateCartItem(CartDto cartDto) throws ResourceNotFoundException;

	String deleteCartItem(Long cartId) throws ResourceNotFoundException;

	List<CartDto> getCartItemsOfAUser(Long id) throws ResourceNotFoundException;

	String deleteCartItemsByUserId(Long userId) throws ResourceNotFoundException;

}
