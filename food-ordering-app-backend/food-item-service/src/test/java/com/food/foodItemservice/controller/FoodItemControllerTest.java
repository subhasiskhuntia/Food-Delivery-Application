package com.food.foodItemservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.foodItemservice.dto.FoodDto;
import com.food.foodItemservice.dto.FoodImageDto;
import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.service.FoodItemService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(FoodItemController.class)
class FoodItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FoodItemService foodItemService;

	private FoodItemDto foodItemDto;
	private FoodDto foodDto;
	private FoodImageDto foodImageDto;

	@BeforeEach
	void setUp() {
		// mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();
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
	void addFoodItem() throws Exception {
		when(foodItemService.addFoodItem(any(FoodItemDto.class))).thenReturn(foodItemDto);

		String foodItemDtoJson = this.convertToJson(foodItemDto);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/foodItem/addFoodItem")
				.contentType(MediaType.APPLICATION_JSON).content(foodItemDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedfoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedfoodItemDtoJson);

	}

	@Test
	public void testAddMultipleFoodItems() throws Exception {
		List<FoodItemDto> foodItems = new ArrayList<>();
		foodItems.add(foodItemDto);
		when(foodItemService.addMultipleFoodItems(any(List.class))).thenReturn(foodItems);

		String foodItemsJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/foodItem/addMultipleFoodItem")
				.contentType(MediaType.APPLICATION_JSON).content(foodItemsJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemsJson, returnedFoodItemDtoJson);

	}

	@Test
	public void testgetAllByRestaurantIds() throws Exception {
		List<FoodItemDto> foodItems = new ArrayList<>();
		foodItems.add(foodItemDto);
		
		List<Long> ids = new ArrayList<>();
		ids.add(1l);
		
		when(foodItemService.getAllByRestaurantIds(ids)).thenReturn(foodItems);

		String foodItemsJson = this.convertToJson(foodItems);
		String restaurantIds=this.convertToJson(ids);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getAllByRestaurantIds")
				.contentType(MediaType.APPLICATION_JSON).content(restaurantIds);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemsJson, returnedFoodItemDtoJson);

	}

	@Test
	public void testgetAllFoodItems() throws Exception {
		List<FoodItemDto> foodItems = new ArrayList<>();
		foodItems.add(foodItemDto);
		when(foodItemService.getAllFoodItems()).thenReturn(foodItems);

		String foodItemsJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/foodItem/getAllFoodItems");

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemsJson, returnedFoodItemDtoJson);

	}

	@Test
	public void testgetAllFoodItemsByRestaurantId() throws Exception {
		List<FoodItemDto> foodItems = new ArrayList<>();
		foodItems.add(foodItemDto);
		when(foodItemService.getAllByRestaurantId(any(Long.class))).thenReturn(foodItems);

		String foodItemsJson = this.convertToJson(foodItems);

		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/v1/foodItem/getAllByRestaurantId/" + foodItemDto.getRestaurantId());

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemsJson, returnedFoodItemDtoJson);

	}

	@Test
	public void testGetSpecificFoodItem() throws Exception {
		when(foodItemService.getSpecificFoodItemById(any(Long.class))).thenReturn(foodItemDto);
		String foodItemDtoJson = this.convertToJson(foodItemDto);

		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/v1/foodItem/getSpecificFoodItem?id=" + foodItemDto.getId());

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemDtoJson, returnedFoodItemDtoJson);

	}

	@Test
	public void testUpdateFoodItemSuccess() throws Exception {
		when(foodItemService.updateFoodItem(any(FoodItemDto.class))).thenReturn(foodItemDto);

		String foodItemJson = this.convertToJson(foodItemDto);

		RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/foodItem/updateFoodItem")
				.contentType(MediaType.APPLICATION_JSON).content(foodItemJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(foodItemJson, returnedFoodItemDtoJson);
	}

	@Test
	void testDeleteFoodItemById() throws Exception {
		when(foodItemService.deleteFoodItemById(anyLong())).thenReturn("Food Item deleted successfully");

		String foodItemJson = this.convertToJson(foodItemDto);

		RequestBuilder request = MockMvcRequestBuilders
				.delete("/api/v1/foodItem/deleteFoodItemById?id=" + foodItemDto.getId());
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals("Food Item deleted successfully", returnedFoodItemDtoJson);

	}
	@Test
	void testfoodItemExistOrNot() throws Exception {
		when(foodItemService.foodItemExistOrNot(any(Long.class))).thenReturn(true);

		String foodItemJson = this.convertToJson(foodItemDto);

		RequestBuilder request = MockMvcRequestBuilders
				.get("/api/v1/foodItem/foodItemExistOrNot?id=" + foodItemDto.getId());

		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		

		int status = mvcResult.getResponse().getStatus();
		System.out.println(status);
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals("true", returnedFoodItemDtoJson);

	}
	@Test
	void testMultipleFoodItemExistOrNot() throws Exception {
		List<Long> ids=new ArrayList<>();
		ids.add(1l);
		when(foodItemService.multipleFoodItemExistOrNot(any(List.class))).thenReturn(true);

		String foodIds = this.convertToJson(ids);

		RequestBuilder request = MockMvcRequestBuilders
				.post("/api/v1/foodItem/multipleFoodItemExistOrNot").contentType(MediaType.APPLICATION_JSON).content(foodIds);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		

		int status = mvcResult.getResponse().getStatus();
		System.out.println(status);
		String returnedFoodItemDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals("true", returnedFoodItemDtoJson);

	}

	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
