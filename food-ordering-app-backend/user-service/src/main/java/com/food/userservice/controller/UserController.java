package com.food.userservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.userservice.dto.UserChangePasswordDto;
import com.food.userservice.dto.UserCredential;
import com.food.userservice.dto.UserDto;
import com.food.userservice.exception.InvalidUsernamOrPasswordException;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RefreshScope
public class UserController {

	public final UserService userService;
	
	@Value("${message}")
	private String message;

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto userDto) {
		User savedUser = this.userService.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserCredential credential)
			throws InvalidUsernamOrPasswordException, ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.validateUser(credential));
	}

	@PatchMapping("/logout/{email}")
	public String logout(@PathVariable("email") String email) throws ResourceNotFoundException {
		return userService.userLogOut(email);
	}

	@GetMapping("/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
			throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
	}

	@PatchMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody @Valid UserChangePasswordDto changePasswordDto)
			throws ResourceNotFoundException, InvalidUsernamOrPasswordException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.changePassword(changePasswordDto));
	}

	@PutMapping("/updateProfile")
	public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto userDto) throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userDto));
	}

	@PatchMapping("/updateRole")
	public ResponseEntity<String> changeUserRole(@RequestParam("email") String email) throws ResourceNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserRole(email));
	}

	@GetMapping(value = "/userExistOrNot/{userId}")
	public ResponseEntity<Boolean> userExistOrNot(@PathVariable long userId) {
		// System.out.println(userService.checkUserExistedOrNot(userId));
		return ResponseEntity.ok(userService.checkUserExistedOrNot(userId));
	}
	
	@GetMapping(value = "/getByUserId/{userId}")
	public ResponseEntity<UserDto> getByUserId(@PathVariable long userId) throws ResourceNotFoundException {
		return ResponseEntity.ok(userService.getByUserId(userId));
	}
	
	@PostMapping(value = "/getMultipleUsersByIds")
	public ResponseEntity<List<UserDto>> getMultipleUsersByIds(@RequestBody List<Long> userIds){
		return ResponseEntity.ok(userService.getMultipleUsersByIds(userIds));
	}
	
	@GetMapping("/configTest")
	public String getConfigMessage() {
		return message;
	}
	
}
