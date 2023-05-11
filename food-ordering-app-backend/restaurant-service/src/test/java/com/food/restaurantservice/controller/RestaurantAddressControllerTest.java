package com.food.restaurantservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.restaurantservice.dto.RestaurantAddressDto;
import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.service.RestaurantAddressServiceImpl;


@WebMvcTest(RestaurantAddressController.class)
class RestaurantAddressControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestaurantAddressServiceImpl restaurantAddressService;


	RestaurantAddressDto restaurantAddressDto;
	
	RestaurantDto restaurantDto;
	
	@BeforeEach
	public void setUp() {
		restaurantAddressDto = new RestaurantAddressDto();

		restaurantAddressDto = RestaurantAddressDto.builder().id(1).city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573).build();
		
		restaurantDto=new RestaurantDto();
		restaurantDto=RestaurantDto.builder().id(1).name("kfc").approvalStatus(true).closedAt("06:30:00").openedAt("12:00:00").
				address(restaurantAddressDto).description("biriyan, fries").mobileNumber("9951376741").userEmail("anil@gmail.com").build();
		
	}

	@AfterEach
	public void tearDown() {
		restaurantAddressDto = null;
	}


	@Test
	@DisplayName("Test getRestaurantInACity method of RestaurantAdressController class")
	void testGetRestaurantsInACity() throws Exception{
		List<RestaurantDto> list=new ArrayList<>();
		list.add(restaurantDto);
		when(restaurantAddressService.getRestaurantsInACity(any(String.class))).thenReturn(list);
		String restaurantDtoJson = this.convertToJson(list);
		

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/restaurant/getRestaurantInACity/?city=Lucknow")
				.contentType(MediaType.APPLICATION_JSON).content(restaurantDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedRestaurantDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals(restaurantDtoJson, returnedRestaurantDtoJson);
	}
	

	@Test
	@DisplayName("Test getNearByRestaurant method of RestaurantAdressController class")
	void testGetNearByRestaurants() throws Exception {
		List<RestaurantDto> list=new ArrayList<>();
		list.add(restaurantDto);
		when(restaurantAddressService.getNearByRestaurant(any(String.class),any(String.class))).thenReturn(list);
		String restaurantDtoJson = this.convertToJson(list);
			
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/restaurant/getNearByRestaurants")
				.contentType(MediaType.APPLICATION_JSON).content(restaurantDtoJson).param("state", "Uttear Pradesh").param("latitude", "30.6577").param("longitude", "76.8573");

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedRestaurantDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals(restaurantDtoJson, returnedRestaurantDtoJson);
	}

	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
