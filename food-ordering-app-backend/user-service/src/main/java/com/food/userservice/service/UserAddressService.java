package com.food.userservice.service;

import java.util.List;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.exception.ResourceNotFoundException;

public interface UserAddressService {
	public UserAddressDto addAddress(UserAddressDto userAddressDto) throws ResourceNotFoundException;

	public List<UserAddressDto> getUserAddressesByEmail(String email) throws ResourceNotFoundException;

	public UserAddressDto changeUserAddress(UserAddressDto userAddressDto) throws ResourceNotFoundException;

	public Boolean addressExistByIdOrNot(Long id);

	public String deleteById(long addressId) throws ResourceNotFoundException;

	public UserAddressDto getAddressById(Long addressId) throws ResourceNotFoundException;
	
	public List<UserAddressDto> getMultipleUserAddressIds(List<Long> userAddressIds);
}
