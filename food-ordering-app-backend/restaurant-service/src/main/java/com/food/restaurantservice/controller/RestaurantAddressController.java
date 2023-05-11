package com.food.restaurantservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.service.RestaurantAddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
public class RestaurantAddressController {

	private final RestaurantAddressService restaurantAddressService;

	@GetMapping("/getRestaurantInACity")
	public ResponseEntity<List<RestaurantDto>> getRestaurantsInACity(@RequestParam("city") String city)
			throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantAddressService.getRestaurantsInACity(city));
	}

	@GetMapping("/getNearByRestaurants")
	public ResponseEntity<List<RestaurantDto>> getNearByRestaurants(
			@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude)
			throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantAddressService.getNearByRestaurant(latitude, longitude));
	}

}
