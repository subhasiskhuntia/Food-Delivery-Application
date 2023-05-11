package com.food.orderservice.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("user-service/api/v1/user")
public interface UserProxyService {

	@GetMapping(value = "/userExistOrNot/{userId}")
	public ResponseEntity<Boolean> userExistOrNot(@PathVariable long userId);

	@GetMapping(value = "/addressExistByIdOrNot")
	public ResponseEntity<Boolean> addressExistByIdOrNot(@RequestParam("userAddressId") Long id);

	@PostMapping(value = "/getMultipleUserAddressesByIds")
	public ResponseEntity<List<Map<String, Object>>> getMultipleUserAddressIds(@RequestBody List<Long> userAddressIds);

	@PostMapping(value = "/getMultipleUsersByIds")
	public ResponseEntity<List<Map<String, Object>>> getMultipleUsersByIds(@RequestBody List<Long> userIds);

	@GetMapping(value = "/getByUserId/{userId}")
	public ResponseEntity<Map<String, Object>> getByUserId(@PathVariable long userId);
}
