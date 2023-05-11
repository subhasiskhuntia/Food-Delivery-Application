package com.food.cartservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service/api/v1/user")
public interface UserProxyService {
	@GetMapping(value = "/userExistOrNot/{userId}")
	public ResponseEntity<Boolean> userExistOrNot(@PathVariable long userId);

}
