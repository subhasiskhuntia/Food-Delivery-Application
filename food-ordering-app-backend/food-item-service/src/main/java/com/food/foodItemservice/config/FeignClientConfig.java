package com.food.foodItemservice.config;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.food.foodItemservice.feign.RestaurantProxyService;

@Configuration
public class FeignClientConfig {
	
	@Bean
	public RestaurantProxyService restaurantProxyService() {
		return new RestaurantProxyService() {
			
			@Override
			public ResponseEntity<Boolean> restaurantExistOrNotById(long id) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
