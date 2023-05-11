package com.food.orderservice.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.food.orderservice.exception.ResourceNotFoundException;


@Service
@FeignClient("food-item-service/api/v1/foodItem")
public interface FoodItemProxyService {

	@PostMapping(value = "/multipleFoodItemExistOrNot")
	public ResponseEntity<Boolean> multipleFoodItemExistOrNot(@RequestBody List<Long> ids);
	@GetMapping(value = "/getFoodItemById")
	public ResponseEntity<Map<String, Object>> getFoodItemById(@RequestParam("id") Long id) throws ResourceNotFoundException;
	@PostMapping(value = "/getMultipleFoodItemByIds")
	public ResponseEntity<List<Map<String, Object>>> getMultipleFoodItemByIds(@RequestBody List<Long> ids);

}
