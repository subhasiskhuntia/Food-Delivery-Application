package com.food.foodItemservice.utils;

import com.food.foodItemservice.dto.FoodDto;
import com.food.foodItemservice.dto.FoodImageDto;
import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.Food;
import com.food.foodItemservice.entity.FoodItem;
import com.food.foodItemservice.entity.FoodItemImage;

public class FoodItemServiceMapping {
	public static FoodItem mapFoodItemDtoToFoodItem(FoodItemDto foodItemDto) {
		FoodItem foodItem = FoodItem.builder().id(foodItemDto.getId()).description(foodItemDto.getDescription())
				.price(foodItemDto.getPrice()).restaurantId(foodItemDto.getRestaurantId())
				.food(FoodItemServiceMapping.mapFoodDtoToFood(foodItemDto.getFood())).build();
		if (foodItemDto.getImage() != null)
			foodItem.setImage(FoodItemServiceMapping.mapFoodItemImageDtoToFoodItemImage(foodItemDto.getImage()));
		return foodItem;
	}

	public static FoodItemDto mapFoodItemToFoodItemDto(FoodItem foodItem) {
		FoodItemDto foodItemDto = FoodItemDto.builder().id(foodItem.getId()).description(foodItem.getDescription())
				.price(foodItem.getPrice()).restaurantId(foodItem.getRestaurantId())
				.food(FoodItemServiceMapping.mapFoodToFoodDto(foodItem.getFood())).build();
		if (foodItem.getImage() != null)
			foodItemDto.setImage(FoodItemServiceMapping.mapFoodItemImageToFoodItemImageDto(foodItem.getImage()));
		return foodItemDto;
	}

	public static Food mapFoodDtoToFood(FoodDto foodDto) {
		return Food.builder().id(foodDto.getId()).foodName(foodDto.getFoodName()).cuisine(foodDto.getCuisine())
				.mealType(foodDto.getMealType()).build();
	}

	public static FoodDto mapFoodToFoodDto(Food food) {
		return FoodDto.builder().id(food.getId()).foodName(food.getFoodName()).cuisine(food.getCuisine())
				.mealType(food.getMealType()).build();
	}

	public static FoodImageDto mapFoodItemImageToFoodItemImageDto(FoodItemImage foodItemImage) {
		return FoodImageDto.builder().id(foodItemImage.getId()).imageName(foodItemImage.getImageName())
				.image(foodItemImage.getImage()).build();
	}

	public static FoodItemImage mapFoodItemImageDtoToFoodItemImage(FoodImageDto foodItemImageDto) {
		return FoodItemImage.builder().id(foodItemImageDto.getId()).imageName(foodItemImageDto.getImageName())
				.image(foodItemImageDto.getImage()).build();
	}

}
