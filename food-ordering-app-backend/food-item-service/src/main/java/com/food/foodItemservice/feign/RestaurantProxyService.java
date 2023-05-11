package com.food.foodItemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient("restaurant-service//api/v1/restaurant")
public interface RestaurantProxyService {
	@GetMapping(value = "/existOrNot/{id}")
	public ResponseEntity<Boolean> restaurantExistOrNotById(@PathVariable("id") long id);
}
