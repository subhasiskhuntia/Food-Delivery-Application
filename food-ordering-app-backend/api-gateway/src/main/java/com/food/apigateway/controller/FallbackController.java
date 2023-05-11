package com.food.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	@GetMapping("/fallback")
	public String fallback() {
		return "The server is currently down. Please try again after sometime.";
	}

}
