package com.food.restaurantservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("user-service/api/v1/user")
public interface ProxyService {

	@GetMapping("/{email}")
	public Object getUserByEmail(@PathVariable("email") String email);

}
