package com.food.orderservice.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("restaurant-service/api/v1/restaurant")
@Service
public interface RestaurantProxyService {
	@GetMapping(value = "/{id}")
	public ResponseEntity<Map<String, Object>> getRestaurantById(@PathVariable(value = "id") long id);

	@PostMapping(value = "/getMultipleRestaurantByIds")
	public ResponseEntity<List<Map<String, Object>>> getMultipleRestaurantByIds(@RequestBody List<Long> restaurantIds);
}
