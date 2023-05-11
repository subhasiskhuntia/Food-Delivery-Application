package com.food.foodItemservice.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.food.foodItemservice.dto.FoodItemDto;
import com.food.foodItemservice.entity.FoodItem;
import com.food.foodItemservice.exception.ResourceNotFoundException;
import com.food.foodItemservice.service.FoodItemService;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/foodItem")
public class FoodItemController {
	private final FoodItemService foodItemService;

	@PostMapping(value = "/addFoodItem")
	public ResponseEntity<FoodItemDto> addFoodItem(@RequestBody @Valid FoodItemDto foodItemDto)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodItemService.addFoodItem(foodItemDto));
	}
	
	@GetMapping("/getAllByRestaurantIds")
	public ResponseEntity<List<FoodItemDto>> getAllByRestaurantIds(@RequestBody List<Long> ids) throws ResourceNotFoundException{
		return ResponseEntity.ok(foodItemService.getAllByRestaurantIds(ids));
				
	}

	@PostMapping(value = "/addMultipleFoodItem")
	public ResponseEntity<List<FoodItemDto>> addMultipleFoodItem(@RequestBody @Valid List<FoodItemDto> foodItemDtos)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodItemService.addMultipleFoodItems(foodItemDtos));
	}

	@GetMapping(value = "/getSpecificFoodItem")
	public ResponseEntity<FoodItemDto> getSpecificFoodItem(@RequestParam("id") Long id)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodItemService.getSpecificFoodItemById(id));
	}
	
	@GetMapping(value = "/getAllFoodItems")
	public ResponseEntity<List<FoodItemDto>> getAllFoodItems()
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodItemService.getAllFoodItems());
	}

	@PutMapping(value = "/updateFoodItem")
	public ResponseEntity<FoodItemDto> updateFoodItem(@RequestBody FoodItemDto foodItemDto)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(foodItemService.updateFoodItem(foodItemDto));
	}

	@DeleteMapping(value = "/deleteFoodItemById")
	public ResponseEntity<String> deleteFoodItemById(@RequestParam("id") Long id) {
		return ResponseEntity.ok(foodItemService.deleteFoodItemById(id));
	}
	
	@GetMapping("/getAllByRestaurantId/{restaurantId}")
	public ResponseEntity<List<FoodItemDto>> getAllByRestaurantId(@PathVariable("restaurantId") Long restaurantId) throws ResourceNotFoundException{
		return ResponseEntity.status(HttpStatus.OK).body(foodItemService.getAllByRestaurantId(restaurantId));
	}
	
	@GetMapping(value = "/foodItemExistOrNot")
	public ResponseEntity<Boolean> foodItemExistOrNot(@RequestParam("id") Long id){
	    return ResponseEntity.ok(foodItemService.foodItemExistOrNot(id));
	}
	
	@PostMapping(value = "/multipleFoodItemExistOrNot")
	public ResponseEntity<Boolean> multipleFoodItemExistOrNot(@RequestBody List<Long> ids){
	    return ResponseEntity.ok(foodItemService.multipleFoodItemExistOrNot(ids));
	}
	@GetMapping(value = "/getFoodItemById")
	public ResponseEntity<FoodItemDto> getFoodItemById(@RequestParam("id") Long id) throws ResourceNotFoundException{
		return ResponseEntity.ok(foodItemService.getFoodItemById(id));
	}
	@PostMapping(value = "/getMultipleFoodItemByIds")
	public ResponseEntity<List<FoodItemDto>> getMultipleFoodItemByIds(@RequestBody List<Long> ids){
		return ResponseEntity.ok(foodItemService.getMultipleFoodItemByIds(ids));
	}
}
