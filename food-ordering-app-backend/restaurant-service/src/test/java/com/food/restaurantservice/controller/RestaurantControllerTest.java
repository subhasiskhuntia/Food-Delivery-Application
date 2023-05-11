package com.food.restaurantservice.controller;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import com.food.restaurantservice.service.RestaurantService;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestaurantService restaurantService;

	RestaurantAddressDto restaurantAddressDto;

	RestaurantDto restaurantDto;

	@BeforeEach
	public void setUp() {
		restaurantAddressDto = new RestaurantAddressDto();

		restaurantAddressDto = RestaurantAddressDto.builder().id(1).city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").latitude(30.6577).longitude(76.8573)
				.build();

		restaurantDto = new RestaurantDto();
		restaurantDto = RestaurantDto.builder().id(1).name("kfc").approvalStatus(true).closedAt("06:30:00")
				.openedAt("12:00:00").address(restaurantAddressDto).description("biriyani fries")
				.mobileNumber("9951376741").userEmail("anil@gmail.com").build();
		
		MockitoAnnotations.initMocks(this);



	}

	@AfterEach
	public void tearDown() {
		restaurantAddressDto = null;
	}

	@Test
	@DisplayName("Test register method of RestaurantController class")
	void testRegister() throws Exception{
		
		when(restaurantService.register(any(RestaurantDto.class))).thenReturn("Restaurant Saved Successfully");
		
		
		String restaurantDtoJson = this.convertToJson(restaurantDto);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/restaurant/register")
				.contentType(MediaType.APPLICATION_JSON).content(restaurantDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedRestaurantDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(201, status);
		assertEquals("Restaurant Saved Successfully", returnedRestaurantDtoJson);
	}

	@Test
	@DisplayName("Test getRestaurantById method of RestaurantController class")
	void testGetRestaurantById()throws Exception {
		when(restaurantService.findRestaurantById(any(Long.class))).thenReturn(restaurantDto);
		String restaurantDtoJson = this.convertToJson(restaurantDto);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/restaurant/"+restaurantDto.getId())
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedRestaurantDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(returnedRestaurantDtoJson, returnedRestaurantDtoJson);
		
		
	}

	@Test
	void testGetRestaurantByEmail()throws Exception {
		when(restaurantService.findRestaurantByEmail(any(String.class))).thenReturn(restaurantDto);
		String restaurantDtoJson = this.convertToJson(restaurantDto);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/restaurant/email?email="+restaurantDto.getUserEmail());
				

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedRestaurantDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(returnedRestaurantDtoJson, returnedRestaurantDtoJson);
		
	}

	@Test
	void testRestaurantExistOrNotById() throws Exception{
		when(restaurantService.restaurantExistOrNotById(any(Long.class))).thenReturn(true);
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/restaurant/existOrNot/"+restaurantDto.getId());
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		assertEquals("true",mvcResult.getResponse().getContentAsString());
		
	}
	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
