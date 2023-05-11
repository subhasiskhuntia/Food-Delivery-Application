package com.food.userservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
import com.food.userservice.controller.UserAddressController;
import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.dto.UserDto;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.service.UserAddressService;
import com.food.userservice.service.UserService;

@WebMvcTest(UserAddressController.class)
class UserAddressControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserAddressService userAddressService;


	UserAddressDto userAddressDto;

	@BeforeEach
	public void setUp() {
		userAddressDto = new UserAddressDto();

		userAddressDto = UserAddressDto.builder().id(1).email("aquib@gmail.com").city("Lucknow").state("Uttar Pradesh")
				.pincode(226012).houseNumber("123/A").locality("Aashiyana").build();
	}

	@AfterEach
	public void tearDown() {
		userAddressDto = null;
	}

	@Test
	@DisplayName("Test for addUserAddress method of UserAddressController class.")
	void testAddUserAddress() throws Exception {
		when(userAddressService.addAddress(any(UserAddressDto.class))).thenReturn(userAddressDto);
		String userAddressDtoJson = this.convertToJson(userAddressDto);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/user/addAddress")
				.contentType(MediaType.APPLICATION_JSON).content(userAddressDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserAddressDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(201, status);
		assertEquals(userAddressDtoJson, returnedUserAddressDtoJson);
	}

	@Test
	@DisplayName("Test for getUserAddressesByEmail method of UserAddressController class.")
	void testGetUserAddressesByEmail() throws Exception {
		List<UserAddressDto> userAddresses = new ArrayList<>();
		userAddresses.add(userAddressDto);
		

		
		when(userAddressService.getUserAddressesByEmail("aquib@gmail.com")).thenReturn(userAddresses);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user/getAddress")
				.contentType(MediaType.APPLICATION_JSON).param("email", "aquib@gmail.com");
		
		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserAddresses = mvcResult.getResponse().getContentAsString();
		
		assertEquals(200, status);
		assertEquals(convertToJson(userAddresses), returnedUserAddresses);
		
		
	}


	@Test
	@DisplayName("Test for changeUserAddresses method of UserAddressController class.")
	void testChangeUserAddress() throws Exception {
		when(userAddressService.changeUserAddress(any(UserAddressDto.class))).thenReturn(userAddressDto);
		String userAddressDtoJson = this.convertToJson(userAddressDto);

		RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/user/changeAddress")
				.contentType(MediaType.APPLICATION_JSON).content(userAddressDtoJson);
		

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserAddressDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals(userAddressDtoJson, returnedUserAddressDtoJson);
	}
	@Test
	@DisplayName("Test for userAddressExistOrNot method of UserAddressController class.")
	void testUserAddressExistOrNot() throws Exception {
		when(userAddressService.addressExistByIdOrNot(any(Long.class))).thenReturn(true);
		String userAddressDtoJson = this.convertToJson(userAddressDto);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user//addressExistByIdOrNot/?userAddressId="+userAddressDto.getId())
				.contentType(MediaType.APPLICATION_JSON).content(userAddressDtoJson);
		

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserAddressDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals("true", returnedUserAddressDtoJson);
	}
	@Test
	@DisplayName("false Test for userAddressExistOrNot method of UserAddressController class.")
	void falseTstUserAddressExistOrNot() throws Exception {
		when(userAddressService.addressExistByIdOrNot(any(Long.class))).thenReturn(false);
		String userAddressDtoJson = this.convertToJson(userAddressDto);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user//addressExistByIdOrNot/?userAddressId="+userAddressDto.getId())
				.contentType(MediaType.APPLICATION_JSON).content(userAddressDtoJson);
		

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserAddressDtoJson = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals("false", returnedUserAddressDtoJson);
	}



	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
