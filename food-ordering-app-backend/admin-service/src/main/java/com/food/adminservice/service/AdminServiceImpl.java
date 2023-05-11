package com.food.adminservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.food.adminservice.dto.AdminDto;
import com.food.adminservice.exception.InvalidUsernamOrPasswordException;
import com.food.adminservice.exception.ResourceNotFoundException;
import com.food.adminservice.feign.RestauranProxyService;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class AdminServiceImpl implements AdminService {

	@Value("${admin.email}")
	private String email;

	@Value("${admin.password}")
	private String password;

	@Autowired
	private RestauranProxyService restauranProxyService;

	@Override
	@CircuitBreaker(fallbackMethod = "showRestaurantsFallback", name = "fallBackForShowRestaurants")
	public Object showRestaurants() throws ResourceNotFoundException {
		try {
			return restauranProxyService.getAllRestaurants();
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("No Restaurants in the database.");
		}

	}

	public Object showRestaurantsFallback(RuntimeException exception) throws ResourceNotFoundException {
		Map<String, String> defaultResponse = new HashMap<>();
		defaultResponse.put("Message", "Restaurant Service is currently down. Please try again sometime.");
		return defaultResponse;
	}

	@Override
	@CircuitBreaker(fallbackMethod = "changeRestaurantStatusFallback", name = "fallBackForChangeRestaurantStatus")
	public String changeRestaurantStatus(Long restaurantId) throws ResourceNotFoundException {
		try {
			return restauranProxyService.changeApprovalStatus(restaurantId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("No Restaurant exists with id: " + restaurantId);
		}
	}

	public String changeRestaurantStatusFallback(Long restaurantId, RuntimeException exception)
			throws ResourceNotFoundException {
		return "Restaurant Service is currently down. Please try again sometime.";
	}

	@Override
	@CircuitBreaker(fallbackMethod = "deleteRestaurantFallback", name = "fallBackForDeleteRestaurant")
	public String deleteRestaurant(Long restaurantId) throws ResourceNotFoundException {
		try {
			return restauranProxyService.deleteRestaurantById(restaurantId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("No Restaurant exists with id: " + restaurantId);
		}
	}

	public String deleteRestaurantFallback(Long restaurantId, RuntimeException exception)
			throws ResourceNotFoundException {
		return "Restaurant Service is currently down. Please try again sometime.";
	}

	@Override
	public String login(AdminDto adminDto) throws InvalidUsernamOrPasswordException {
		System.out.println("---------------");
		System.out.println(email);
		System.out.println(password);
		System.out.println("---------------");
		if (!adminDto.getEmail().equals(email) || !adminDto.getPassword().equals(password)) {
			throw new InvalidUsernamOrPasswordException("Invalid Username or Password");
		}
		return "Admin logged in successfully";
	}

}
