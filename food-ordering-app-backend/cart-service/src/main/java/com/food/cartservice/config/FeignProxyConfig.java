package com.food.cartservice.config;
import java.util.Map;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.food.cartservice.exception.ResourceNotFoundException;
import com.food.cartservice.feign.FoodProxyService;
import com.food.cartservice.feign.UserProxyService;

@Configuration
public class FeignProxyConfig {
    
    @Bean
    public UserProxyService userProxyService() {
    	return new UserProxyService() {
			
			@Override
			public ResponseEntity<Boolean> userExistOrNot(long userId) {
				// TODO Auto-generated method stub
				return null;
			}
		};
    }
    @Bean
    public FoodProxyService foodproxyService() {
    	return new FoodProxyService() {
			
			@Override
			public ResponseEntity<Boolean> foodItemExistOrNot(Long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResponseEntity<Map<String, Object>> getFoodItemById(Long id) throws ResourceNotFoundException {
				// TODO Auto-generated method stub
				return null;
			}
		};
    }
}

