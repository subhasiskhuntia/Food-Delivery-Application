package com.food.foodItemservice.service;

import java.util.List;

import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.exception.ResourceNotFoundException;

public interface FoodService {

	List<FoodItemDto> getFoodItemsByFoodName(String name) throws ResourceNotFoundException;

	List<String> getDistinctFoodName();

	List<FoodItemDto> getFoodItemsByMealsType(String mealType);

	List<FoodItemDto> getFoodByCuisineType(String cuisineType);

}
