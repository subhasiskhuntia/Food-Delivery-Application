package com.food.foodItemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.food.foodItemservice")
@EnableEurekaClient
@SpringBootApplication
public class FoodItenServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodItenServiceApplication.class, args);
	}

}
