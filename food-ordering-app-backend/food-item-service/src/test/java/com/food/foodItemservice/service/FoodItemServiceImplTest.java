package com.food.foodItemservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

class FoodItemServiceImplTest {

	@Mock
	private FoodItemRepository foodItemRepository;
	@Mock
	private FoodRepository foodRepository;
	@Mock 
	private FoodItemImageRepository foodItemImageRepository;
	@Mock
	private RestaurantProxyService restaurantProxyService;
	
	private FoodItemService foodItemService;
	
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
		
		foodItemService=new FoodItemServiceImpl(foodItemRepository,foodRepository,restaurantProxyService);

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

	}

	@Test
	@DisplayName("test addFoodItem method of FoodItemServiceImpl class")
	void testAddFoodItem() throws Exception{
		Optional<Food> foodOptional=Optional.ofNullable(food);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodRepository.findFoodByFoodNameAndMealType(any(String.class), any(String.class))).thenReturn(foodOptional);
		when(foodItemRepository.save(any(FoodItem.class))).thenReturn(foodItem);
		
		FoodItemDto foodItemDto1=foodItemService.addFoodItem(foodItemDto);
		
		assertNotNull(foodItemDto1);
	}
	@Test
	@DisplayName("test addFoodItem method of FoodItemServiceImpl class")
	void testAddFoodItemWhenFoodWasNull() throws Exception{
		Optional<Food> foodOptional=Optional.ofNullable(null);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodRepository.findFoodByFoodNameAndMealType(any(String.class), any(String.class))).thenReturn(foodOptional);
		when(foodItemRepository.save(any(FoodItem.class))).thenReturn(foodItem);
		
		FoodItemDto foodItemDto1=foodItemService.addFoodItem(foodItemDto);
		
		assertNotNull(foodItemDto1);
	}
	@Test
	@DisplayName("test addFoodItem method when there is no food item of given foodname and mealtype of FoodItemServiceImpl class")
	void testAddFoodItemResourceNotFoundExceptyion() throws Exception{
		Optional<Food> foodOptional=Optional.ofNullable(food);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		
		assertThrows(ResourceNotFoundException.class,()->foodItemService.addFoodItem(foodItemDto));
	}

	@Test
	@DisplayName("negative test addMultipleItems method when list is empty of FoodServiecImpl class")
	void testAddMultipleFoodItemsWhenListIsEmpty() {
		List<FoodItemDto> foodItemsDtos=new ArrayList<>();
		assertThrows(ResourceNotFoundException.class, ()->foodItemService.addMultipleFoodItems(foodItemsDtos));
	}
	@Test
	@DisplayName("negative test addMultipleItems method when list is empty of FoodServiecImpl class")
	void testAddMultipleFoodItemsWhenRestaurantNotFound() {
		List<FoodItemDto> foodItemsDtos=new ArrayList<>();
		foodItemsDtos.add(foodItemDto);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		assertThrows(ResourceNotFoundException.class, ()->foodItemService.addMultipleFoodItems(foodItemsDtos));
	}
	@Test
	@DisplayName("test addMultipleItems method of FoodServiecImpl class")
	void testAddMultipleFoodItems()throws Exception {
		List<FoodItemDto> foodItemsDtos=new ArrayList<>();
		foodItemsDtos.add(foodItemDto);
		Optional<Food> foodOptional=Optional.ofNullable(food);
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodRepository.findFoodByFoodNameAndMealType(any(String.class), any(String.class))).thenReturn(foodOptional);
		when(foodItemRepository.saveAll(any(List.class))).thenReturn(foodItems);
		
		assertNotNull(foodItemService.addMultipleFoodItems(foodItemsDtos));
	}
	
	@Test
	@DisplayName("test addMultipleItems method when food is empty of FoodServiecImpl class")
	void testAddMultipleFoodItemsWhenFoodisEmpty()throws Exception {
		List<FoodItemDto> foodItemsDtos=new ArrayList<>();
		foodItemsDtos.add(foodItemDto);
		Optional<Food> foodOptional=Optional.ofNullable(null);
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(restaurantProxyService.restaurantExistOrNotById(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodRepository.findFoodByFoodNameAndMealType(any(String.class), any(String.class))).thenReturn(foodOptional);
		when(foodItemRepository.saveAll(any(List.class))).thenReturn(foodItems);
		
		assertNotNull(foodItemService.addMultipleFoodItems(foodItemsDtos));
	}

	@Test
	@DisplayName("negative test getSpecificFoodItemById method of FoodServiecImpl class")
	void testGetSpecificFoodItemByIdResourceNotFoundException() {
		Optional<FoodItem> foodOptional=Optional.ofNullable(null);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		assertThrows(ResourceNotFoundException.class, ()->foodItemService.getSpecificFoodItemById(foodItemDto.getId()));
	}
	@Test
	@DisplayName("test getSpecificFoodItemById method of FoodServiecImpl class")
	void testGetSpecificFoodItemById()throws Exception {
		Optional<FoodItem> foodOptional=Optional.ofNullable(foodItem);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		assertNotNull(foodItemService.getSpecificFoodItemById(foodItemDto.getId()));
	}

	@Test
	@DisplayName("negative test upadateFoodItem method of FoodServiecImpl class")
	void testUpdateFoodItemResourceNotFoundException() {
		Optional<FoodItem> foodOptional=Optional.ofNullable(null);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		assertThrows(ResourceNotFoundException.class, ()->foodItemService.updateFoodItem(foodItemDto));
	}
	@Test
	@DisplayName("negative test upadateFoodItem method of FoodServiecImpl class")
	void testUpdateFoodItemWhenFoodIdIsNull() {
		FoodItemDto foodItemDto1=foodItemDto;
		foodItemDto1.setId(null);
		assertThrows(ResourceNotFoundException.class, ()->foodItemService.updateFoodItem(foodItemDto1));
	}
	@Test
	@DisplayName("test upadateFoodItem method of FoodServiecImpl class")
	void testUpdateFoodItem()throws Exception {
		Optional<FoodItem> foodOptional=Optional.ofNullable(foodItem);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		when(foodItemRepository.save(any(FoodItem.class))).thenReturn(foodItem);
		assertNotNull(foodItemService.updateFoodItem(foodItemDto));
	}

	@Test
	@DisplayName("test delete method pf foodServiceImpl class")
	void testDeleteFoodItemById() {
		doNothing().when(foodItemRepository).deleteById(any(Long.class));
		assertEquals("FoodItem deleted successfully", foodItemService.deleteFoodItemById(foodItemDto.getId()));
	}

	@Test
	@DisplayName("test getAllByRestaurantById method of FoodServiecImpl class")
	void testGetAllByRestaurantId()throws Exception {
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(foodItemRepository.findAllByRestaurantId(any(Long.class))).thenReturn(foodItems);
		assertNotNull(foodItemService.getAllByRestaurantId(foodItemDto.getRestaurantId()));
	}
	@Test
	@DisplayName("negative test getAllByRestaurantById method of FoodServiecImpl class")
	void testGetAllByRestaurantIdResourceNotFoundException()throws Exception {
		List<FoodItem> foodItems=new ArrayList<>();
		when(foodItemRepository.findAllByRestaurantId(any(Long.class))).thenReturn(foodItems);
		assertThrows(ResourceNotFoundException.class,()->foodItemService.getAllByRestaurantId(foodItemDto.getRestaurantId()));
	}

	@Test
	@DisplayName("true tset for foodItemExistOrNot method of FoodItemServiecImpl class")
	void testFoodItemExistOrNot() {
		Optional<FoodItem> foodOptional=Optional.ofNullable(foodItem);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		assertTrue(foodItemService.foodItemExistOrNot(foodItem.getId()));
	}
	@Test
	@DisplayName("false tset for foodItemExistOrNot method of FoodItemServiecImpl class")
	void flasetestFoodItemExistOrNot() {
		Optional<FoodItem> foodOptional=Optional.ofNullable(null);
		when(foodItemRepository.findById(any(Long.class))).thenReturn(foodOptional);
		assertFalse(foodItemService.foodItemExistOrNot(foodItem.getId()));
	}

	@Test
	@DisplayName("true test for multipleFoodItemExistOrNot method of FoodItemServiecImpl class")
	void testMultipleFoodItemExistOrNot() {
		List<Long> ids=new ArrayList<>();
		ids.add(1l);
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(foodItemRepository.findAllById(any(List.class))).thenReturn(foodItems);
		
		assertTrue(foodItemService.multipleFoodItemExistOrNot(ids));
	}
	@Test
	@DisplayName("false test for multipleFoodItemExistOrNot method of FoodItemServiecImpl class")
	void falseTestMultipleFoodItemExistOrNot() {
		List<Long> ids=new ArrayList<>();
		ids.add(1l);
		List<FoodItem> foodItems=new ArrayList<>();
		
		when(foodItemRepository.findAllById(any(List.class))).thenReturn(foodItems);
		
		assertFalse(foodItemService.multipleFoodItemExistOrNot(ids));
	}

	@Test
	@DisplayName(" test for getAllFoodItems method of FoodItemServiecImpl class")
	void testGetAllFoodItems() throws Exception{
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(foodItemRepository.findAll()).thenReturn(foodItems);
		assertNotNull(foodItemService.getAllFoodItems());
	}
	@Test
	@DisplayName("negative test for getAllFoodItems method of FoodItemServiecImpl class")
	void testGetAllFoodItemsResourceNotFoundException() throws Exception{
		List<FoodItem> foodItems=new ArrayList<>();
		when(foodItemRepository.findAll()).thenReturn(foodItems);
		assertThrows(ResourceNotFoundException.class,()->foodItemService.getAllFoodItems());
	}

	@Test
	@DisplayName("test for getAllByRestaurantIds method of FoodItemServiecImpl class")
	void testGetAllByRestaurantIds()throws Exception {
		List<Long> ids=new ArrayList<>();
		ids.add(1l);
		List<FoodItem> foodItems=new ArrayList<>();
		foodItems.add(foodItem);
		when(foodItemRepository.findAllByRestaurantIdIn(any(List.class))).thenReturn(foodItems);
		assertNotNull(foodItemService.getAllByRestaurantIds(ids));
	}
	@Test
	@DisplayName("negative test for getAllByRestaurantIds method of FoodItemServiecImpl class")
	void testGetAllByRestaurantIdsResourceNotFoundException()throws Exception {
		List<Long> ids=new ArrayList<>();
		ids.add(1l);
		List<FoodItem> foodItems=new ArrayList<>();
		when(foodItemRepository.findAllByRestaurantIdIn(any(List.class))).thenReturn(foodItems);
		assertThrows(ResourceNotFoundException.class,()->foodItemService.getAllByRestaurantIds(ids));
	}

}
