package com.food.userservice.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.service.UserAddressService;

import java.util.List;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserAddressController {
	private final UserAddressService userAddressService;

	@PostMapping("/addAddress")
	public ResponseEntity<UserAddressDto> addUserAddress(@RequestBody @Valid UserAddressDto userAddressDto)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(userAddressService.addAddress(userAddressDto));
	}

	@GetMapping("/getAddress")
	public ResponseEntity<List<UserAddressDto>> getUserAddressesByEmail(@RequestParam("email") String email)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userAddressService.getUserAddressesByEmail(email));
	}
	
	@GetMapping("/getAddressById")
	public ResponseEntity<UserAddressDto> getAddressById(@RequestParam("addressId") Long addressId)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userAddressService.getAddressById(addressId));
	}

	@PutMapping("/changeAddress")
	public ResponseEntity<UserAddressDto> changeUserAddress(@RequestBody UserAddressDto userAddressDto)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userAddressService.changeUserAddress(userAddressDto));
	}

	@GetMapping(value = "/addressExistByIdOrNot")
	public ResponseEntity<Boolean> addressExistByIdOrNot(@RequestParam("userAddressId") Long id) {
		return ResponseEntity.ok(userAddressService.addressExistByIdOrNot(id));
	}

	@DeleteMapping("/deleteAddressById/{addressId}")
	public ResponseEntity<String> deleteAddressById(@PathVariable("addressId") long addressId)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userAddressService.deleteById(addressId));
	}
	
	@PostMapping(value = "/getMultipleUserAddressesByIds")
	public ResponseEntity<List<UserAddressDto>> getMultipleUserAddressIds(@RequestBody List<Long> userAddressIds) {
		return ResponseEntity.ok(userAddressService.getMultipleUserAddressIds(userAddressIds));
	}
}
