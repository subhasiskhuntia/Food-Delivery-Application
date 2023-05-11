package com.food.adminservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.adminservice.dto.AdminDto;
import com.food.adminservice.exception.InvalidUsernamOrPasswordException;
import com.food.adminservice.exception.ResourceNotFoundException;
import com.food.adminservice.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;

	@GetMapping(value = "/showRestaurants")
	public ResponseEntity<Object> showRestaurants() throws ResourceNotFoundException {
		return ResponseEntity.ok(adminService.showRestaurants());
	}

	@PatchMapping(value = "/changeApprovalStatus")
	public ResponseEntity<String> changeApprovalStatus(@RequestParam("restaurantId") long restaurantId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(adminService.changeRestaurantStatus(restaurantId));
	}

	@DeleteMapping(value = "/deleteRestaurant")
	public ResponseEntity<String> deleteRestaurant(@RequestParam("restaurantId") long restaurantId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(adminService.deleteRestaurant(restaurantId));
	}

	@PostMapping(value = "/login")
	public ResponseEntity<String> Login(@RequestBody @Valid AdminDto adminDto)throws InvalidUsernamOrPasswordException {
		return ResponseEntity.ok(adminService.login(adminDto));
	}
}
