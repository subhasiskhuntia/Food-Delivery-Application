package com.food.foodItemservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/foodItem")
@RequiredArgsConstructor
public class FoodController {

	private final FoodService foodService;

	@GetMapping(value = "/getFoodByName")
	public ResponseEntity<List<FoodItemDto>> getFoodItemsByFoodName(@RequestParam("foodName") String name)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodService.getFoodItemsByFoodName(name));
	}

	@GetMapping(value = "/getDistinctFoodName")
	public ResponseEntity<List<String>> getDistinctFoodName() {
		return ResponseEntity.ok(foodService.getDistinctFoodName());
	}

	@GetMapping(value = "/getFoodByMealType")
	public ResponseEntity<List<FoodItemDto>> getFoodByMealType(@RequestParam("mealType") String mealType) {
		return ResponseEntity.ok(foodService.getFoodItemsByMealsType(mealType));
	}

	@GetMapping(value = "/getFoodByCuisineType")
	public ResponseEntity<List<FoodItemDto>> getFoodByCuisineType(@RequestParam("cuisineType") String cuisineType) {
		return ResponseEntity.ok(foodService.getFoodByCuisineType(cuisineType));
	}

}
