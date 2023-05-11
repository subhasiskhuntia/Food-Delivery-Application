package com.food.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("cart-service/api/v1/cart")
public interface CartProxyService {

	@DeleteMapping(value = "/deleteCartItemsByUserId")
	public String deleteCartItemsByUserId(@RequestParam("userId") Long userId);

}
