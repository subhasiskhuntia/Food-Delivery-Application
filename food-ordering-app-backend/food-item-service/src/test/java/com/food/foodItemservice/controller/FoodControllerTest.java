package com.food.foodItemservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.foodItemservice.dto.FoodDto;
import com.food.foodItemservice.dto.FoodImageDto;
import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.service.FoodService;

@WebMvcTest(FoodController.class)
public class FoodControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private FoodService foodService;
	
	FoodDto foodDto;
	FoodItemDto foodItemDto;
	FoodImageDto foodImageDto;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		foodDto = new FoodDto();
		foodDto = FoodDto.builder().foodName("test-food").cuisine("test-cuisine").mealType("test-meal-type").build();
		foodImageDto = new FoodImageDto();
		FoodImageDto foodImageDto = FoodImageDto.builder().imageName("test-image-url").build();
		foodItemDto = new FoodItemDto();
		foodItemDto = FoodItemDto.builder().id(1L).price(10.99F).description("test-food-item").restaurantId(1L)
				.food(foodDto).image(foodImageDto).build();
	}

	@Test
	public void testGetFoodItemsByFoodName() throws Exception {

		List<FoodItemDto> foodItems=new ArrayList<FoodItemDto>();
		foodItems.add(foodItemDto);
		when(foodService.getFoodItemsByFoodName(any(String.class))).thenReturn(foodItems);

		String foodItemDtoJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getFoodByName?foodName="+foodDto.getFoodName());
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedfoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedfoodItemDtoJson);
	}

	@Test
	public void testGetDistinctFoodName()throws Exception {
		List<String> foodItems=new ArrayList<>();
		foodItems.add("Biriyani");
		when(foodService.getDistinctFoodName()).thenReturn(foodItems);

		String foodItemDtoJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getDistinctFoodName");
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedfoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedfoodItemDtoJson);
	}

	@Test
	public void testGetFoodByMealType()throws Exception {
		
		List<FoodItemDto> foodItems=new ArrayList<FoodItemDto>();
		foodItems.add(foodItemDto);
		when(foodService.getFoodItemsByMealsType(any(String.class))).thenReturn(foodItems);

		String foodItemDtoJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getFoodByMealType?mealType="+foodDto.getMealType());
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedfoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedfoodItemDtoJson);
	}

	@Test
	public void testGetFoodByCuisineType()throws Exception {
	
		List<FoodItemDto> foodItems=new ArrayList<FoodItemDto>();
		foodItems.add(foodItemDto);
		when(foodService.getFoodByCuisineType(any(String.class))).thenReturn(foodItems);

		String foodItemDtoJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getFoodByCuisineType?cuisineType="+foodDto.getCuisine());
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedfoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedfoodItemDtoJson);
	}
	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
}