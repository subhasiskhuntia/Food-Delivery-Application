package com.food.foodItemservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.food.foodItemservice.dto.FoodDto;
import com.food.foodItemservice.dto.FoodImageDto;
import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.Food;
import com.food.foodItemservice.entity.FoodItem;
import com.food.foodItemservice.entity.FoodItemImage;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.feign.RestaurantProxyService;
import com.food.foodItemservice.repository.FoodItemImageRepository;
import com.food.foodItemservice.repository.FoodItemRepository;
import com.food.foodItemservice.repository.FoodRepository;

class FoodServiceImplTest {
	
	@Mock
	private FoodItemRepository foodItemRepository;
	@Mock
	private FoodRepository foodRepository;
	@Mock 
	private FoodItemImageRepository foodItemImageRepository;
	@Mock
	private RestaurantProxyService restaurantProxyService;
	
	private FoodService foodService;
	
	Food food;
	FoodItemImage foodItemImage;
	FoodItem foodItem;
	FoodDto foodDto;
	FoodItemDto foodItemDto;
	FoodImageDto foodImageDto;

	@BeforeEach
	void setUp() {
		// mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();
		MockitoAnnotations.initMocks(this);
		
		foodService=new FoodServiceImpl(foodRepository);

		foodDto = new FoodDto();
		foodDto = FoodDto.builder().foodName("test-food").cuisine("test-cuisine").mealType("test-meal-type").build();
		foodImageDto = new FoodImageDto();
		FoodImageDto foodImageDto = FoodImageDto.builder().imageName("test-image-url").build();
		foodItemDto = new FoodItemDto();
		foodItemDto = FoodItemDto.builder().id(1L).price(10.99F).description("test-food-item").restaurantId(1L)
				.food(foodDto).image(foodImageDto).build();

		food = new Food();
		food = Food.builder().id(1L).foodName("Pizza").cuisine("Italian").mealType("Lunch").build();
		foodItemImage = new FoodItemImage();
		foodItemImage = FoodItemImage.builder().id(1l).image("biriyani.jpej").imageName("biriyani").build();
		foodItem = new FoodItem();
		foodItem = FoodItem.builder().id(1L).price(10.99f).description("Pepperoni Pizza").restaurantId(1L).food(food)
				.image(foodItemImage).build();
		List<FoodItem> foodItems=new ArrayList<FoodItem>();
		foodItems.add(foodItem);
		food.setFoodItems(foodItems);

	}


	@Test
	@DisplayName("test getFoodItemsByFoodName method of FoodService class")
	void testGetFoodItemsByFoodName() throws Exception{
		Optional<Food> optionalfood=Optional.ofNullable(food);
		when(foodRepository.findFoodByFoodName(any(String.class))).thenReturn(optionalfood);
		assertNotNull(foodService.getFoodItemsByFoodName(food.getFoodName()));
	}
	@Test
	@DisplayName("negative test getFoodItemsByFoodName method of FoodService class")
	void nagativeTestGetFoodItemsByFoodName() throws Exception{
		Optional<Food> optionalfood=Optional.ofNullable(null);
		when(foodRepository.findFoodByFoodName(any(String.class))).thenReturn(optionalfood);
		assertThrows(ResourceNotFoundException.class,()->foodService.getFoodItemsByFoodName(food.getFoodName()));
	}

	@Test
	@DisplayName(" test getDistinctFoodName method of FoodService class")
	void testGetDistinctFoodName() {
		List<String> foods=new ArrayList<>();
		foods.add("biriyani");
		when(foodRepository.findDistinctFoodName()).thenReturn(foods);
		assertEquals(foods, foodService.getDistinctFoodName());
	}

	@Test
	void testGetFoodItemsByMealsType() {
		List<Food> foods=new ArrayList<Food>();
		foods.add(food);
		when(foodRepository.findFoodByMealType(any(String.class))).thenReturn(foods);
		assertNotNull(foodService.getFoodItemsByMealsType(food.getMealType()));
	}

	@Test
	void testGetFoodByCuisineType() {
		List<Food> foods=new ArrayList<Food>();
		foods.add(food);
		when(foodRepository.findFoodByCuisine(any(String.class))).thenReturn(foods);
		assertNotNull(foodService.getFoodByCuisineType(food.getCuisine()));
	}

}
