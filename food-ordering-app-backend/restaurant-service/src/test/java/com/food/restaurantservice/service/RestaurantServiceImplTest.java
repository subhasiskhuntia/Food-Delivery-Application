package com.food.restaurantservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.food.restaurantservice.dto.RestaurantAddressDto;
import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.exceptions.UserNotFoundException;
import com.food.restaurantservice.feign.ProxyService;
import com.food.restaurantservice.model.Restaurant;
import com.food.restaurantservice.model.RestaurantAddress;
import com.food.restaurantservice.repository.RestaurantAddressRepository;
import com.food.restaurantservice.repository.RestaurantRepository;


import feign.FeignException;

class RestaurantServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private RestaurantAddressRepository restaurantAddressRepository;

	@Mock
	private ProxyService proxyService;

	
	private RestaurantServiceImpl restaurantService;

	RestaurantDto restaurantDto;

	RestaurantAddress restaurantAddress;
	RestaurantAddressDto restaurantAddressDto;

	Restaurant restaurant;

	@BeforeEach
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		restaurantService=new RestaurantServiceImpl(restaurantRepository, restaurantAddressRepository, proxyService);
		restaurantAddressDto = RestaurantAddressDto.builder().id(1).city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573)
				.build();
		restaurantAddress = new RestaurantAddress();

		restaurantAddress = RestaurantAddress.builder().id(1).city("Lucknow").state("Uttar Pradesh").pincode(226012)
				.houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573).build();
		restaurantDto = new RestaurantDto();
		restaurantDto = RestaurantDto.builder().id(1).name("kfc").approvalStatus(true).closedAt("06:30:00")
				.openedAt("12:00:00").address(restaurantAddressDto).description("biriyani fries")
				.mobileNumber("9951376741").userEmail("anil@gmail.com").build();

		restaurant = new Restaurant();
		restaurant = Restaurant.builder().id(1).name("kfc").approvalStatus(true).createdOn(LocalDateTime.now())
				.closedOn(LocalTime.now()).openedOn(LocalTime.now()).address(restaurantAddress)
				.description("biriyan, fries").mobileNumber("9951376741").userId(1).updatedOn(LocalDateTime.now())
				.build();
		restaurantAddress.setRestaurant(restaurant);
		restaurant.setAddress(restaurantAddress);

	}

	@AfterEach
	public void tearDown() {
		restaurantAddressDto = null;
	}

	@Test
	@DisplayName("test register of ResturantServiceImpl class")
	public void testRegister() throws Exception {
		
		Map<String, Object> user = new HashMap<>();
		user.put("id", 123L);
		when(proxyService.getUserByEmail(any(String.class))).thenReturn(user);
		when(restaurantRepository.findRestaurantByUserId(any(Long.class))).thenReturn(null);
		when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
		when(restaurantAddressRepository.save(any(RestaurantAddress.class))).thenReturn(restaurantAddress);

		String result = restaurantService.register(restaurantDto);

		assertEquals("Restaurant Saved Successfully", result);
	}

	@Test
	@DisplayName("Negative test register of ResturantServiceImpl class")
	public void testRegisterRestaurantAlreadyExistsException() throws Exception {
	
		Map<String, Object> user = new HashMap<>();
		user.put("id", 123L);
		when(proxyService.getUserByEmail(any(String.class))).thenReturn(user);
		when(restaurantRepository.findRestaurantByUserId(any(Long.class))).thenReturn(restaurant);
		when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
		when(restaurantAddressRepository.save(any(RestaurantAddress.class))).thenReturn(restaurantAddress);		

		assertThrows(RestaurantAlreadyExistsException.class ,()-> restaurantService.register(restaurantDto));
	}
	@Test
	@DisplayName("Negative test register of ResturantServiceImpl class")
	public void testRegisterUserNotFoundException() throws Exception {
	
	
		Map<String, Object> user = new HashMap<>();
		user.put("id", 123L);
		when(proxyService.getUserByEmail(any(String.class))).thenThrow(FeignException.NotFound.class);
		
		assertThrows(UserNotFoundException.class ,()-> restaurantService.register(restaurantDto));
	}
//
	@Test
	@DisplayName("test findRestaurantById of ResturantServiceImpl class")
	void testFindRestaurantById() throws Exception {
		Optional<Restaurant> restaurantOptional = Optional.ofNullable(restaurant);
		when(restaurantRepository.findById(any(Long.class))).thenReturn(restaurantOptional);

		RestaurantDto restaurantDto1 = restaurantService.findRestaurantById(restaurant.getId());
		assertNotNull(restaurantDto1);
	}

	@Test
	@DisplayName("Negative test findRestaurantById of ResturantServiceImpl class")
	void testFindRestaurantByIRestaurantNotFoundEception() throws Exception {
		Optional<Restaurant> restaurantOptional = Optional.ofNullable(null);
		when(restaurantRepository.findById(any(Long.class))).thenReturn(restaurantOptional);

		assertThrows(RestaurantNotFoundException.class, () -> restaurantService.findRestaurantById(restaurant.getId()));
	}

	@Test
	@DisplayName("test findRestaurantByEmail of ResturantServiceImpl class")
	void testFindRestaurantByEmail() throws Exception {
		
		Map<String, Object> user = new HashMap<>();
		user.put("id", 123L);
		when(proxyService.getUserByEmail(any(String.class))).thenReturn(user);
		when(restaurantRepository.findRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		RestaurantDto restaurantDto1 = restaurantService.findRestaurantByEmail(restaurantDto.getUserEmail());

		assertNotNull(restaurantDto1);

	}
//
	@Test
	@DisplayName("negative test findRestaurantByEmail of ResturantServiceImpl class")
	void testFindRestaurantByEmailRestaurantNotFoundException() throws Exception {
		
		Map<String, Object> user = new HashMap<>();
		user.put("id", 123L);
		when(proxyService.getUserByEmail(any(String.class))).thenReturn(user);
		when(restaurantRepository.findRestaurantByUserId(any(Long.class))).thenReturn(null);

		assertThrows(RestaurantNotFoundException.class,
				() -> restaurantService.findRestaurantByEmail(restaurantDto.getUserEmail()));

	}

	@Test
	@DisplayName("test restaurantExistOrNotById of ResturantServiceImpl class")
	void testRestaurantExistOrNotById() {
		Optional<Restaurant> restaurantOptional = Optional.ofNullable(restaurant);
		when(restaurantRepository.findById(any(Long.class))).thenReturn(restaurantOptional);

		assertTrue(restaurantService.restaurantExistOrNotById(restaurant.getId()));
	}

}
