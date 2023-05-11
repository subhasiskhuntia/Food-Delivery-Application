package com.food.userservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.model.UserAddress;
import com.food.userservice.repository.UserAddressRepository;
import com.food.userservice.repository.UserLogRepository;
import com.food.userservice.repository.UserRepository;

class UserAddressServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserAddressRepository userAddressRepository;

	@InjectMocks
	private UserAddressServiceImpl userAddressService;

	UserAddressDto userAddressDto;

	UserAddress userAddress;

	User user;
	Optional<User> userOptional;

	@BeforeEach
	public void setUp() {

		userAddressDto = UserAddressDto.builder().id(1).email("aquib@gmail.com").city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").build();

		MockitoAnnotations.initMocks(this);

		user = new User();
		user.setId((long) 1);
		user.setFullName("Aquib Azhar");
		user.setMobileNumber("9029182812");
		user.setEmail("aquib@gmail.com");
		user.setUsername("aquibazhar");
		user.setPassword("Aquib@123");

		userOptional = Optional.ofNullable(user);

		userAddress = UserAddress.builder().id(1).user(user).city("Lucknow").state("Uttar Pradesh").pincode(226012)
				.houseNumber("123/A").locality("Aashiyana").build();
	}

	@AfterEach
	public void tearDown() {
		user = null;
		userAddressDto = null;
	}

	@Test
	@DisplayName("Test for addAddress method of UserAddressService")
	void testAddAddress() throws Exception {
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);

		UserAddressDto returneUserAddressDto = userAddressService.addAddress(userAddressDto);

		assertEquals(userAddressDto, returneUserAddressDto);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Test for addAddress method when user has atleast one existing address of UserAddressService")
	void testAddAddressWhenAddressIsNotEmpty() throws Exception {
		List<UserAddress> addresses = new ArrayList<>();
		addresses.add(userAddress);

		user.setUserAddress(addresses);

		Optional<User> newUserOptional = Optional.ofNullable(user);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(newUserOptional);

		UserAddressDto returneUserAddressDto = userAddressService.addAddress(userAddressDto);

		assertEquals(userAddressDto, returneUserAddressDto);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Negative Test for addAddress method of UserAddressService")
	void testAddAddressFor() throws Exception {
		Optional<User> emptyUserOptional = Optional.ofNullable(null);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(emptyUserOptional);

		assertThrows(ResourceNotFoundException.class, () -> userAddressService.addAddress(userAddressDto));

		verify(userRepository, times(1)).findByEmail(user.getEmail());

	}

	@Test
	@DisplayName("Test for getUserAddressByEmail method of UserAddressService")
	void testGetUserAddressesByEmail() throws Exception {
		List<UserAddress> userAddressList = new ArrayList<>();
		userAddressList.add(userAddress);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		when(userAddressRepository.findByUser(user)).thenReturn(userAddressList);

		List<UserAddressDto> returnedUserAddresses = userAddressService.getUserAddressesByEmail("aquib@gmail.com");

		assertEquals(userAddressList, returnedUserAddresses);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userAddressRepository, times(1)).findByUser(user);
	}

	@Test
	@DisplayName("Negative Test for getUserAddressByEmail method of UserAddressService")
	void testGetUserAddressesByEmailForResourceNotFoundException() throws Exception {
		List<UserAddress> userAddressList = new ArrayList<>();
		userAddressList.add(userAddress);
		Optional<User> emptyUserOptional = Optional.ofNullable(null);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(emptyUserOptional);
		when(userAddressRepository.findByUser(user)).thenReturn(userAddressList);

		assertThrows(ResourceNotFoundException.class,
				() -> userAddressService.getUserAddressesByEmail("aquib@gmail.com"));

		verify(userRepository, times(1)).findByEmail(user.getEmail());

	}


	@Test
	@DisplayName("Test for changeUserAddress method of UserAddressService")
	void testChangeUserAddress() throws Exception {
		Optional<UserAddress> userAddressOptional = Optional.ofNullable(userAddress);
		when(userAddressRepository.findById(userAddressDto.getId())).thenReturn(userAddressOptional);

		UserAddressDto newUserAddressDto = UserAddressDto.builder().id(1).email("aquib@gmail.com").city("Hyderabad")
				.state("Uttar Pradesh").pincode(226012).houseNumber("123/A").locality("Aashiyana").build();

		UserAddressDto returnedUserAddressDto = userAddressService.changeUserAddress(newUserAddressDto);
		userAddressDto.setCity("Hyderabad");
		assertEquals(userAddressDto, returnedUserAddressDto);

		verify(userAddressRepository, times(1)).findById(userAddressDto.getId());
		verify(userAddressRepository, times(1)).save(userAddress);
	}

	@Test
	@DisplayName("Negative Test for changeUserAddress method of UserAddressService")
	void testChangeUserAddressForResourceNotFoundException() throws Exception {
		Optional<UserAddress> emptyUserAddressOptional = Optional.ofNullable(null);
		when(userAddressRepository.findById(userAddressDto.getId())).thenReturn(emptyUserAddressOptional);

		UserAddressDto newUserAddressDto = UserAddressDto.builder().id(1).email("aquib@gmail.com").city("Hyderabad")
				.state("Uttar Pradesh").pincode(226012).houseNumber("123/A").locality("Aashiyana").build();

		userAddressDto.setCity("Hyderabad");

		assertThrows(ResourceNotFoundException.class, () -> userAddressService.changeUserAddress(newUserAddressDto));

		verify(userAddressRepository, times(1)).findById(userAddressDto.getId());

	}
	@Test
	@DisplayName("Test for userAddressExistOrNot method of UserAddressService")
	void testuserAddressExistOrNot() throws Exception {
		Optional<UserAddress> emptyUserAddressOptional = Optional.ofNullable(userAddress);
		when(userAddressRepository.findById(userAddressDto.getId())).thenReturn(emptyUserAddressOptional);

		assertTrue(userAddressService.addressExistByIdOrNot(userAddressDto.getId()));
		verify(userAddressRepository, times(1)).findById(userAddressDto.getId());

	}
	@Test
	@DisplayName("Flase Test for userAddressExistOrNot method of UserAddressService")
	void falsetestuserAddressExistOrNot() throws Exception {
		Optional<UserAddress> emptyUserAddressOptional = Optional.ofNullable(null);
		when(userAddressRepository.findById(userAddressDto.getId())).thenReturn(emptyUserAddressOptional);

		assertFalse(userAddressService.addressExistByIdOrNot(userAddressDto.getId()));
		verify(userAddressRepository, times(1)).findById(userAddressDto.getId());

	}

}
