package com.food.userservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
import com.food.userservice.controller.UserController;
import com.food.userservice.dto.UserChangePasswordDto;
import com.food.userservice.dto.UserCredential;
import com.food.userservice.dto.UserDto;
import com.food.userservice.model.User;
import com.food.userservice.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	User user;

	UserDto userDto;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setId(1);
		user.setFullName("Aquib Azhar");
		user.setMobileNumber("9029182812");
		user.setEmail("aquib@gmail.com");
		user.setUsername("aquibazhar");
		user.setPassword("Aquib@123");

		userDto = new UserDto();
		userDto.setId(userDto.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setMobileNumber(userDto.getMobileNumber());
		userDto.setUsername(user.getUsername());
		userDto.setPassword(user.getPassword());
	}

	@AfterEach
	public void tearDown() {
		user = null;
	}

	@Test
	@DisplayName("Test for createUser method of UserController class.")
	void testCreateUser() throws Exception {

		when(userService.createUser(any(UserDto.class))).thenReturn(user);
		String userJson = this.convertToJson(user);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/user/register")
				.contentType(MediaType.APPLICATION_JSON).content(userJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserJson = mvcResult.getResponse().getContentAsString();

		assertEquals(201, status);
		assertEquals(userJson, returnedUserJson);

	}

	@Test
	@DisplayName("Test for loginUser method of UserController class.")
	void testLoginUser() throws Exception {
		UserCredential userCredential = new UserCredential();
		userCredential.setEmail("aquib@gmail.com");
		userCredential.setPassword("Aquib@123");

		when(userService.validateUser(any(UserCredential.class))).thenReturn("User logged in successfully");
		String userCredentialJson = this.convertToJson(userCredential);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/user/login")
				.contentType(MediaType.APPLICATION_JSON).content(userCredentialJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedMessage = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals("User logged in successfully", returnedMessage);

	}

	@Test
	@DisplayName("Test for changePassword method of UserController class.")
	void testChangePassword() throws Exception {
		UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto();
		userChangePasswordDto.setEmail("aquib@gmail.com");
		userChangePasswordDto.setOldPassword("Aquib@123");
		userChangePasswordDto.setNewPassword("Aquib@12345");

		when(userService.changePassword(any(UserChangePasswordDto.class))).thenReturn("Password changed successfully");
		String userChangePasswordDtoJson = this.convertToJson(userChangePasswordDto);

		RequestBuilder request = MockMvcRequestBuilders.patch("/api/v1/user/changePassword")
				.contentType(MediaType.APPLICATION_JSON).content(userChangePasswordDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedMessage = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals("Password changed successfully", returnedMessage);
	}

	@Test
	@DisplayName("Test for getUserByEmail method of UserController class.")
	void testGetUserByEmail() throws Exception {

		when(userService.getUserByEmail(any(String.class))).thenReturn(userDto);
		String userDtoJson = this.convertToJson(userDto);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
				.param("email", user.getEmail());

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserDto = mvcResult.getResponse().getContentAsString();
		System.out.print(returnedUserDto);

		//assertEquals(200, status);
		assertEquals(userDtoJson, returnedUserDto);
	}

	@Test
	@DisplayName("Test for updateProfile method of UserController class.")
	void testUpdateProfile() throws Exception {
		when(userService.updateProfile(any(UserDto.class))).thenReturn(userDto);

		String userDtoJson = this.convertToJson(userDto);

		RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/user/updateProfile")
				.contentType(MediaType.APPLICATION_JSON).content(userDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedUserDto = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals(userDtoJson, returnedUserDto);
	}

	@Test
	@DisplayName("Test for logout method of UserController class.")
	void testLogout() throws Exception {
		when(userService.userLogOut(user.getEmail())).thenReturn("Logged out successfully");
		
		RequestBuilder request = MockMvcRequestBuilders.patch("/api/v1/user/logout/{email}", user.getEmail())
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String returnedMessage = mvcResult.getResponse().getContentAsString();

		assertEquals(200, status);
		assertEquals("Logged out successfully", returnedMessage);
	}
	@Test
	@DisplayName("true Test for userExistOrNot method of UserController class.")
	void testUserExistOrNot() throws Exception {
		when(userService.checkUserExistedOrNot(any(Long.class))).thenReturn(true);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user/userExistOrNot/{userId}", user.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String returnedMessage = mvcResult.getResponse().getContentAsString();
		System.out.println(returnedMessage);
		assertEquals(200, status);
		assertEquals("true", returnedMessage);
	}
	@Test
	@DisplayName("false Test for userExistOrNot method of UserController class.")
	void fasletestUserExistOrNot() throws Exception {
		when(userService.checkUserExistedOrNot(any(Long.class))).thenReturn(false);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user/userExistOrNot/{userId}", user.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(request).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String returnedMessage = mvcResult.getResponse().getContentAsString();
		System.out.println(returnedMessage);
		assertEquals(200, status);
		assertEquals("false", returnedMessage);
	}

	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
