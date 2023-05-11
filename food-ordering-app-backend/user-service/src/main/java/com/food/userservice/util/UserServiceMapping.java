package com.food.userservice.util;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.dto.UserDto;
import com.food.userservice.model.User;
import com.food.userservice.model.UserAddress;

public class UserServiceMapping {
	public static UserDto mapUserToUserDto(User user) {
		return UserDto.builder().id(user.getId()).username(user.getUsername()).fullName(user.getFullName())
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).role(user.getRole()).build();
	}

	public static User mapUserDtoToUser(UserDto dto) {
		return User.builder().id(dto.getId()).email(dto.getEmail()).fullName(dto.getFullName())
				.mobileNumber(dto.getMobileNumber()).password(dto.getPassword()).username(dto.getUsername())
				.role(dto.getRole()).build();
	}

	public static UserAddressDto mapUserAddressToUserAddressDto(UserAddress userAddress) {
		return UserAddressDto.builder().city(userAddress.getCity()).houseNumber(userAddress.getHouseNumber())
				.id(userAddress.getId()).locality(userAddress.getLocality()).state(userAddress.getState())
				.pincode(userAddress.getPincode()).email(userAddress.getUser().getEmail()).build();
	}

	public static UserAddress mapUserAddressDtoToUserAddress(UserAddressDto userAddressDto) {
		return UserAddress.builder().city(userAddressDto.getCity()).houseNumber(userAddressDto.getHouseNumber())
				.id(userAddressDto.getId()).locality(userAddressDto.getLocality()).state(userAddressDto.getState())
				.pincode(userAddressDto.getPincode()).build();
	}

}
