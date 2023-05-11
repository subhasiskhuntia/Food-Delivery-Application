package com.food.restaurantservice.service;

import java.util.List;

import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.exceptions.UserNotFoundException;

public interface RestaurantService {

	String register(RestaurantDto restaurantDto) throws RestaurantAlreadyExistsException, UserNotFoundException;

	RestaurantDto findRestaurantById(long id) throws RestaurantNotFoundException;

	RestaurantDto findRestaurantByEmail(String email) throws RestaurantNotFoundException;

	Boolean restaurantExistOrNotById(long id);

	List<RestaurantDto> getAllRestaurants() throws RestaurantNotFoundException;

	String changeApprovalStatus(Long restaurantId) throws RestaurantNotFoundException;

	String deleteRestaurantById(Long restaurantId) throws RestaurantNotFoundException;
	
	List<RestaurantDto> getMultipleRestaurantsByIds(List<Long> restaurantIds);

}
