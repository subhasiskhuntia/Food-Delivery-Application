package com.food.userservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.model.UserAddress;
import com.food.userservice.repository.UserAddressRepository;
import com.food.userservice.repository.UserRepository;
import com.food.userservice.util.UserServiceMapping;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
	private final UserAddressRepository addressRepository;
	private final UserRepository userRepository;

	@Override
	public UserAddressDto addAddress(UserAddressDto userAddressDto) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(userAddressDto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException("User not found having email: " + userAddressDto.getEmail()));

		if (user.getUserAddress() == null) {
			user.setUserAddress(Arrays.asList());
			user.setUserAddress(Arrays.asList(UserAddress.builder().city(userAddressDto.getCity())
					.houseNumber(userAddressDto.getHouseNumber()).locality(userAddressDto.getLocality())
					.pincode(userAddressDto.getPincode()).state(userAddressDto.getState()).user(user).build()));
		} else {
			user.getUserAddress()
					.add(UserAddress.builder().city(userAddressDto.getCity())
							.houseNumber(userAddressDto.getHouseNumber()).locality(userAddressDto.getLocality())
							.pincode(userAddressDto.getPincode()).state(userAddressDto.getState()).user(user).build());
		}
		userRepository.save(user);
		return userAddressDto;
	}

	@Override
	public List<UserAddressDto> getUserAddressesByEmail(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found having email: " + email));

		return addressRepository.findByUser(user).stream()
				.map(userAddress -> mapUserAddressToUserAddressDto(userAddress)).collect(Collectors.toList());
	}

	@Override
	public UserAddressDto changeUserAddress(UserAddressDto userAddressDto) throws ResourceNotFoundException {
		UserAddress address = addressRepository.findById(userAddressDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Address with this ID doesn't exist"));

		address.setCity(userAddressDto.getCity());
		address.setHouseNumber(userAddressDto.getHouseNumber());
		address.setLocality(userAddressDto.getLocality());
		address.setPincode(userAddressDto.getPincode());
		address.setState(userAddressDto.getState());
		addressRepository.save(address);
		return userAddressDto;
	}

	@Override
	public Boolean addressExistByIdOrNot(Long id) {
		return addressRepository.findById(id).isPresent();
	}

	public UserAddressDto mapUserAddressToUserAddressDto(UserAddress userAddress) {

		return UserAddressDto.builder().id(userAddress.getId()).city(userAddress.getCity())
				.state(userAddress.getState()).houseNumber(userAddress.getHouseNumber())
				.locality(userAddress.getLocality()).email(userAddress.getUser().getEmail())
				.pincode(userAddress.getPincode()).build();

	}

	@Override
	public String deleteById(long addressId) throws ResourceNotFoundException {
		UserAddress address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address with this ID doesn't exist"));

		addressRepository.deleteById(addressId);

		return "Address deleted successfully";
	}

	@Override
	public UserAddressDto getAddressById(Long addressId) throws ResourceNotFoundException {
		UserAddress userAddress= addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address with this ID doesn't exist"));
		return UserAddressDto.builder().id(userAddress.getId()).city(userAddress.getCity())
				.state(userAddress.getState()).houseNumber(userAddress.getHouseNumber())
				.locality(userAddress.getLocality()).email(userAddress.getUser().getEmail())
				.pincode(userAddress.getPincode()).build();
	}
	
	public List<UserAddressDto> getMultipleUserAddressIds(List<Long> userAddressIds) {
		return addressRepository.findAllById(userAddressIds).stream().map(userAddress->UserServiceMapping.mapUserAddressToUserAddressDto(userAddress)).collect(Collectors.toList());
	}
}