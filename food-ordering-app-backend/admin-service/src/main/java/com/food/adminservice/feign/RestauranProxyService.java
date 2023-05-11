package com.food.adminservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("restaurant-service/api/v1/restaurant")
public interface RestauranProxyService {
	@GetMapping(value = "/getAllRestaurants")
	public Object getAllRestaurants();

	@PutMapping("/changeApprovalStatus")
	public String changeApprovalStatus(@RequestParam("restaurantId") long restaurantId);
	
	@DeleteMapping("/deleteRestaurantById")
	public String deleteRestaurantById(@RequestParam("restaurantId") long restaurantId);
}
