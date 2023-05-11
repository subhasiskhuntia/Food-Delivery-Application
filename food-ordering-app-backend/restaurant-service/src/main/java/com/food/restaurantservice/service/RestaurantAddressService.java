package com.food.restaurantservice.service;

import java.util.List;

import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;

public interface RestaurantAddressService {

	List<RestaurantDto> getRestaurantsInACity(String city) throws RestaurantNotFoundException;
	List<RestaurantDto> getNearByRestaurant(String latitude, String longitude) throws RestaurantNotFoundException;

}
