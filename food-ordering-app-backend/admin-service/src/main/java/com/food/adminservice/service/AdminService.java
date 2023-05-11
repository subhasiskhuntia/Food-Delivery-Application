package com.food.adminservice.service;

import org.springframework.http.ResponseEntity;

import com.food.adminservice.dto.AdminDto;
import com.food.adminservice.exception.InvalidUsernamOrPasswordException;
import com.food.adminservice.exception.ResourceNotFoundException;

public interface AdminService {

	public Object showRestaurants() throws ResourceNotFoundException;

	public String changeRestaurantStatus(Long restaurantId) throws ResourceNotFoundException;

	public String deleteRestaurant(Long restaurantId) throws ResourceNotFoundException;

	public String login(AdminDto adminDto) throws InvalidUsernamOrPasswordException;

}
