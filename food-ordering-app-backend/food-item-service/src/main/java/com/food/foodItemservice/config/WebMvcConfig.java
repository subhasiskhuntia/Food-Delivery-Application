package com.food.foodItemservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig  implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {

		// 1. Allow this for all urls (/**)
		// 2. Allow all origins (*)
		// 3. Allowed Http Methods
		corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH",
				"OPTIONS");
	}
}
