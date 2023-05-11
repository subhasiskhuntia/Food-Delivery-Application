package com.food.restaurantservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.exceptions.UserNotFoundException;
import com.food.restaurantservice.feign.ProxyService;
import com.food.restaurantservice.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/restaurant")
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RestaurantController {

	@Autowired
	private final RestaurantService restaurantService;

	@PostMapping(value = "/register")
	public ResponseEntity<String> register(@RequestBody @Valid RestaurantDto restaurantDto)
			throws RestaurantAlreadyExistsException, UserNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body((restaurantService.register(restaurantDto)));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable(value = "id") long id)
			throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantService.findRestaurantById(id));
	}

	@GetMapping(value = "/email")
	public ResponseEntity<RestaurantDto> getRestaurantByEmail(@RequestParam("email") String email)
			throws RestaurantNotFoundException {
		System.out.println(email);
		return ResponseEntity.ok(restaurantService.findRestaurantByEmail(email));
	}

	@GetMapping(value = "/existOrNot/{id}")
	public ResponseEntity<Boolean> restaurantExistOrNotById(@PathVariable("id") long id) {
		return ResponseEntity.ok(restaurantService.restaurantExistOrNotById(id));
	}

	@GetMapping(value = "/getAllRestaurants")
	public ResponseEntity<List<RestaurantDto>> getAllRestaurants() throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}

	@PutMapping("/changeApprovalStatus")
	public ResponseEntity<String> changeApprovalStatus(@RequestParam("restaurantId") Long restaurantId)
			throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantService.changeApprovalStatus(restaurantId));
	}
	
	@DeleteMapping("/deleteRestaurantById")
	public ResponseEntity<String> deleteRestaurantById(@RequestParam("restaurantId") Long restaurantId)
			throws RestaurantNotFoundException {
		return ResponseEntity.ok(restaurantService.deleteRestaurantById(restaurantId));
	}
	
	@PostMapping(value = "/getMultipleRestaurantByIds")
	public ResponseEntity<List<RestaurantDto>> getMultipleRestaurantByIds(@RequestBody List<Long> restaurantIds){
		return ResponseEntity.ok(restaurantService.getMultipleRestaurantsByIds(restaurantIds));
	}
}
