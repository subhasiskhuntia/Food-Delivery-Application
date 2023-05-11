package com.food.orderservice.config;

import java.util.List;
import java.util.Map;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.food.orderservice.exception.ResourceNotFoundException;
import com.food.orderservice.feign.FoodItemProxyService;
import com.food.orderservice.feign.RestaurantProxyService;
import com.food.orderservice.feign.UserProxyService;

@Configuration
public class FeignClientConfig {
	@Bean
	public RestaurantProxyService restaurantProxyService() {
		return new RestaurantProxyService() {

			@Override
			public ResponseEntity<Map<String, Object>> getRestaurantById(long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<List<Map<String, Object>>> getMultipleRestaurantByIds(List<Long> restaurantIds) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Bean
	public UserProxyService proxyService() {
		return new UserProxyService() {

			@Override
			public ResponseEntity<Boolean> userExistOrNot(long userId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<Boolean> addressExistByIdOrNot(Long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<List<Map<String, Object>>> getMultipleUsersByIds(List<Long> userIds) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<List<Map<String, Object>>> getMultipleUserAddressIds(List<Long> userAddressIds) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<Map<String, Object>> getByUserId(long userId) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Bean
	public FoodItemProxyService foodItemProxyService() {
		return new FoodItemProxyService() {

			@Override
			public ResponseEntity<Boolean> multipleFoodItemExistOrNot(List<Long> ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<Map<String, Object>> getFoodItemById(Long id) throws ResourceNotFoundException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<List<Map<String, Object>>> getMultipleFoodItemByIds(List<Long> ids) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
