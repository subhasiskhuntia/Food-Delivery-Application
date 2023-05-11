//package com.food.restaurantservice.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import com.food.restaurantservice.feign.ProxyService;
//
//@Configuration
//public class FeignClientConfig {
//	@Bean
//	public ProxyService proxyService() {
//		return new ProxyService() {
//			@Override
//			public Object getUserByEmail(String email) {
//				return null;
//			}
//
//		};
//
//	}
//}
