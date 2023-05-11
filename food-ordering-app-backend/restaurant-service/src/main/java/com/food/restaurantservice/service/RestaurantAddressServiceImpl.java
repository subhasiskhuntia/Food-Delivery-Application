package com.food.restaurantservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.model.Restaurant;
import com.food.restaurantservice.repository.RestaurantAddressRepository;
import com.food.restaurantservice.repository.RestaurantRepository;
import com.food.restaurantservice.utils.AppUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantAddressServiceImpl implements RestaurantAddressService {

	@Autowired
	private final RestaurantAddressRepository restaurantAddressRepository;
	@Autowired
	private final RestaurantRepository restaurantRepository;

	@Override
	public List<RestaurantDto> getRestaurantsInACity(String city) throws RestaurantNotFoundException {

		List<Restaurant> restaurants = restaurantRepository.findRestaurantsByACityName(city);
		if (restaurants.size() == 0) {
			throw new RestaurantNotFoundException("No restaurant found in your " + city);
		}
		return restaurants.stream().map(restaurant -> AppUtils.mapRestaurantToRestaurantDto(restaurant, null))
				.collect(Collectors.toList());
	}

	@Override
	public List<RestaurantDto> getNearByRestaurant(String latitude, String longitude)
			throws RestaurantNotFoundException {
		List<Long> restaurantsIds = restaurantAddressRepository.getNearByRestaurants(latitude, longitude);
		if (restaurantsIds.size() == 0)
			throw new RestaurantNotFoundException("No Restaurant found in your area");
		List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantsIds);
		return restaurants.stream().map(restaurant -> AppUtils.mapRestaurantToRestaurantDto(restaurant, null))
				.collect(Collectors.toList());
	}


}
