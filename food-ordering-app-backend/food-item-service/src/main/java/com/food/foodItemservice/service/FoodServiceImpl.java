package com.food.foodItemservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.Food;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.repository.FoodRepository;
import com.food.foodItemservice.utils.FoodItemServiceMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
	
	private final FoodRepository foodRepository;

	@Override
	public List<FoodItemDto> getFoodItemsByFoodName(String name) throws ResourceNotFoundException {
		Food food = foodRepository.findFoodByFoodName(name.trim().toLowerCase())
				.orElseThrow(() -> new ResourceNotFoundException("No Such food Existed as of now"));
		return food.getFoodItems().stream().map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getDistinctFoodName() {
		return foodRepository.findDistinctFoodName();
	}

	@Override
	public List<FoodItemDto> getFoodItemsByMealsType(String mealType) {
		List<FoodItemDto> foodItemDtos = new ArrayList<>();
		List<Food> foodList = foodRepository.findFoodByMealType(mealType);
		for (Food food : foodList) {
			foodItemDtos.addAll(food.getFoodItems().stream()
					.map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
					.collect(Collectors.toList()));
		}
		return foodItemDtos;
	}

	@Override
	public List<FoodItemDto> getFoodByCuisineType(String cuisineType) {
		List<FoodItemDto> foodItemDtos = new ArrayList<>();
		List<Food> foodList = foodRepository.findFoodByCuisine(cuisineType);
		for (Food food : foodList) {
			foodItemDtos.addAll(food.getFoodItems().stream()
					.map(foodItem -> FoodItemServiceMapping.mapFoodItemToFoodItemDto(foodItem))
					.collect(Collectors.toList()));
		}
		return foodItemDtos;
	}
}
