package com.food.cartservice.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.food.cartservice.exception.ResourceNotFoundException;


@FeignClient("food-item-service/api/v1/foodItem")
public interface FoodProxyService {
	@GetMapping(value = "/foodItemExistOrNot")
	public ResponseEntity<Boolean> foodItemExistOrNot(@RequestParam("id") Long id);
	@GetMapping(value = "/getFoodItemById")
	public ResponseEntity<Map<String, Object>> getFoodItemById(@RequestParam("id") Long id) throws ResourceNotFoundException;

}
