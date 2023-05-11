package com.food.userservice.service;

import java.util.List;

import com.food.userservice.dto.UserChangePasswordDto;
import com.food.userservice.dto.UserCredential;
import com.food.userservice.dto.UserDto;
import com.food.userservice.exception.InvalidUsernamOrPasswordException;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;

public interface UserService {

	public User createUser(UserDto userDto);

	public String validateUser(UserCredential userCredential)
			throws InvalidUsernamOrPasswordException, ResourceNotFoundException;
	public String changePassword(UserChangePasswordDto changePasswordDto)
			throws ResourceNotFoundException, InvalidUsernamOrPasswordException;

	public String userLogOut(String email) throws ResourceNotFoundException;

	public UserDto updateProfile(UserDto userDto) throws ResourceNotFoundException;

	public UserDto getUserByEmail(String email) throws ResourceNotFoundException;

	Boolean checkUserExistedOrNot(long id);

	public String updateUserRole(String email) throws ResourceNotFoundException;

	public UserDto getByUserId(long userId) throws ResourceNotFoundException;


	public List<UserDto> getMultipleUsersByIds(List<Long> userIds);

}
