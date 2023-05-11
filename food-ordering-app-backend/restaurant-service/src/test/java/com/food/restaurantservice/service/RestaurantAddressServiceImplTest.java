package com.food.restaurantservice.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.food.restaurantservice.dto.RestaurantAddressDto;
import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.exceptions.RestaurantNotFoundException;
import com.food.restaurantservice.model.Restaurant;
import com.food.restaurantservice.model.RestaurantAddress;
import com.food.restaurantservice.repository.RestaurantAddressRepository;
import com.food.restaurantservice.repository.RestaurantRepository;

class RestaurantAddressServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private RestaurantAddressRepository restaurantAddressRepository;

	@InjectMocks
	private RestaurantAddressServiceImpl restaurantAddressServiceImpl;

	RestaurantAddressDto restaurantAddressDto;

	RestaurantAddress restaurantAddress;

	Restaurant restaurant;
	Optional<Restaurant> restaurantOptional;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		restaurantAddressDto = RestaurantAddressDto.builder().id(1).city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573).build();
		
		restaurantAddress = new RestaurantAddress();
		
		restaurantAddress = RestaurantAddress.builder().id(1).city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573)
				.build();

		restaurant = new Restaurant();
		restaurant = Restaurant.builder().id(1).name("kfc").approvalStatus(true).createdOn(LocalDateTime.now()).closedOn(LocalTime.now())
				.openedOn(LocalTime.now()).address(restaurantAddress).description("biriyan, fries")
				.mobileNumber("9951376741").userId(1).updatedOn(LocalDateTime.now()).build();
		restaurant.setAddress(restaurantAddress);
		restaurantAddress.setRestaurant(restaurant);

	}

	@AfterEach
	public void tearDown() {
		restaurantAddressDto = null;
	}

	@Test
	@DisplayName("Test getRestaurantInACity method of RestaurantAdressService class")
	void testGetRestaurantsInACity() throws Exception{
		List<Restaurant> list=new ArrayList<>();
		list.add(restaurant);
		when(restaurantRepository.findRestaurantsByACityName(anyString())).thenReturn(list);
		List<RestaurantDto> restaurants=restaurantAddressServiceImpl.getRestaurantsInACity(restaurantAddress.getCity());
		assertEquals(1,restaurants.size() );
		verify(restaurantRepository, times(1)).findRestaurantsByACityName(anyString());
	}
	@Test
	@DisplayName("Negative Test getRestaurantInACity method of RestaurantAdressService class")
	void testGetRestaurantsInACityRestaurantNotFoundException() throws Exception{
		List<Restaurant> list=new ArrayList<>();
		when(restaurantRepository.findRestaurantsByACityName(anyString())).thenReturn(list);
		
		assertThrows(RestaurantNotFoundException.class, ()->restaurantAddressServiceImpl.getRestaurantsInACity(restaurantAddress.getCity()));
		verify(restaurantRepository, times(1)).findRestaurantsByACityName(anyString());
	}

	@Test
	@DisplayName(" Test getNearByRestaurant method of RestaurantAdressService class")
	void testGetNearByRestaurant()throws Exception {
		List<Long> restaurantIds=new ArrayList<>();
		restaurantIds.add((long) 1);
		List<Restaurant> list=new ArrayList<>();
		list.add(restaurant);
		when(restaurantAddressRepository.getNearByRestaurants( anyString(), anyString())).thenReturn(restaurantIds);
		when(restaurantRepository.findAllById(restaurantIds)).thenReturn(list);
		
		assertEquals(1,restaurantAddressServiceImpl.getNearByRestaurant
				("30.6577" ,"76.8573").size());
		
		verify(restaurantAddressRepository, times(1)).getNearByRestaurants( anyString(), anyString());
		verify(restaurantRepository, times(1)).findAllById(anyList());
		
	}
	@Test
	@DisplayName(" Negative Test getNearByRestaurant method of RestaurantAdressService class")
	void testGetNearByRestaurantRestaurantNotFoundException()throws Exception {
		List<Long> restaurantIds=new ArrayList<>();
		List<Restaurant> list=new ArrayList<>();
		when(restaurantAddressRepository.getNearByRestaurants( anyString(), anyString())).thenReturn(restaurantIds);
		when(restaurantRepository.findAllById(restaurantIds)).thenReturn(list);
		
		assertThrows(RestaurantNotFoundException.class, ()->restaurantAddressServiceImpl.getNearByRestaurant("30.6577" ,"76.8573"));
		
		verify(restaurantAddressRepository, times(1)).getNearByRestaurants( anyString(), anyString());
		verify(restaurantRepository, times(0)).findAllById(anyList());
		
	}
}
