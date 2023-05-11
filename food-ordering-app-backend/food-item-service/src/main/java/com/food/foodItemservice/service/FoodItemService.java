package com.food.foodItemservice.service;

import java.util.List;

import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.FoodItem;
import com.food.foodItemservice.exception.ResourceNotFoundException;

public interface FoodItemService {

	FoodItemDto addFoodItem(FoodItemDto foodItemDto) throws ResourceNotFoundException;

	List<FoodItemDto> addMultipleFoodItems(List<FoodItemDto> foodItemDtos) throws ResourceNotFoundException;

	FoodItemDto getSpecificFoodItemById(Long id) throws ResourceNotFoundException;

	String deleteFoodItemById(Long id);

	FoodItemDto updateFoodItem(FoodItemDto foodItemDto) throws ResourceNotFoundException;

	Boolean foodItemExistOrNot(Long id);

	Boolean multipleFoodItemExistOrNot(List<Long> ids);

	List<FoodItemDto> getAllByRestaurantId(Long restaurantId) throws ResourceNotFoundException;

	List<FoodItemDto> getAllFoodItems() throws ResourceNotFoundException;

	List<FoodItemDto> getAllByRestaurantIds(List<Long> ids) throws ResourceNotFoundException;

	FoodItemDto getFoodItemById(Long id) throws ResourceNotFoundException;

	List<FoodItemDto> getMultipleFoodItemByIds(List<Long> ids);

}
