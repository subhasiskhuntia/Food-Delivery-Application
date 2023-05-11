package com.food.userservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.OneToOne;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.food.userservice.dto.UserChangePasswordDto;
import com.food.userservice.dto.UserCredential;
import com.food.userservice.dto.UserDto;
import com.food.userservice.exception.InvalidUsernamOrPasswordException;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.model.UserLog;
import com.food.userservice.repository.UserLogRepository;
import com.food.userservice.repository.UserRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserLogRepository userLogRepository;

	@InjectMocks
	private UserServiceImpl userService;

	User user;

	UserDto userDto;

	UserLog userLog;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		user = new User();
		user.setId((long) 1);
		user.setFullName("Aquib Azhar");
		user.setMobileNumber("9029182812");
		user.setEmail("aquib@gmail.com");
		user.setUsername("aquibazhar");
		user.setPassword("Aquib@123");

		userDto = new UserDto();
		userDto.setId((long) 1);
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setMobileNumber(user.getMobileNumber());
		userDto.setUsername(user.getUsername());
		userDto.setPassword(user.getPassword());

		userLog = UserLog.builder().id(1).user(user).userStatus("ACTIVE").totalVisits(1)
				.lastLoggedIn(LocalDateTime.now()).lastLoggedOut(LocalDateTime.now()).build();
	}

	@AfterEach
	public void tearDown() {
		user = null;
		userDto = null;
	}

	@Test
	@DisplayName("Test for createUser method of UserService.")
	void testCreateUser() {

		when(userRepository.save(any(User.class))).thenReturn(user);

		// When
		User actualUser = userService.createUser(userDto);

		// Then
		assertEquals(user.getEmail(), actualUser.getEmail());
		assertEquals(user.getFullName(), actualUser.getFullName());
		assertEquals(user.getMobileNumber(), actualUser.getMobileNumber());
		assertEquals(user.getUsername(), actualUser.getUsername());
		assertEquals(user.getPassword(), actualUser.getPassword());

		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("Test for validateUser method of UserService.")
	void testValidateUser() throws InvalidUsernamOrPasswordException, ResourceNotFoundException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(userOptional);

		Optional<UserLog> userLogOptional = Optional.ofNullable(userLog);
		when(userLogRepository.findUserLogByUser(user)).thenReturn(userLogOptional);

		when(userLogRepository.save(any(UserLog.class))).thenReturn(userLog);

		UserCredential credential = new UserCredential(user.getEmail(), user.getPassword());

		String message = userService.validateUser(credential);

		assertEquals("User logged in successfully", message);

		verify(userRepository, times(1)).findByEmailAndPassword(user.getEmail(), user.getPassword());
	}

	@Test
	@DisplayName("Test for validateUser when user logged in for the first time method of UserService.")
	void testValidateUserForFirstLogIn() throws InvalidUsernamOrPasswordException, ResourceNotFoundException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(userOptional);

		Optional<UserLog> emptyUserLogOptional = Optional.ofNullable(null);
		when(userLogRepository.findUserLogByUser(user)).thenReturn(emptyUserLogOptional);

		when(userLogRepository.save(any(UserLog.class))).thenReturn(userLog);

		UserCredential credential = new UserCredential(user.getEmail(), user.getPassword());

		String message = userService.validateUser(credential);

		assertEquals("User logged in successfully", message);

		verify(userRepository, times(1)).findByEmailAndPassword(user.getEmail(), user.getPassword());
	}

	@Test
	@DisplayName("Negative Test for validateUser method of UserService.")
	void testValidateUserForInvalidUsernameAndPasswordException() throws InvalidUsernamOrPasswordException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmailAndPassword(user.getEmail(), "Some@123")).thenReturn(userOptional);

		UserCredential credential = new UserCredential(user.getEmail(), user.getPassword());

		assertThrows(InvalidUsernamOrPasswordException.class, () -> userService.validateUser(credential));

		verify(userRepository, times(1)).findByEmailAndPassword(user.getEmail(), user.getPassword());
	}

	@Test
	@DisplayName("Test for changePassword method of UserService.")
	void testChangePassword() throws ResourceNotFoundException, InvalidUsernamOrPasswordException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmail(user.getEmail())).thenReturn(userOptional);
		when(userRepository.save(any(User.class))).thenReturn(user);

		UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(user.getEmail(), user.getPassword(),
				"NewPassword@123");

		String message = userService.changePassword(userChangePasswordDto);

		assertEquals("Password changed successfully", message);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("Negative Test for changePassword method of UserService.")
	void testChangePasswordForResourceNotFoundException() throws ResourceNotFoundException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmail("dummyuser")).thenReturn(userOptional);

		UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(user.getEmail(), user.getPassword(),
				"NewPassword@123");

		assertThrows(ResourceNotFoundException.class, () -> userService.changePassword(userChangePasswordDto));

		verify(userRepository, times(1)).findByEmail(user.getEmail());

	}

	@Test
	@DisplayName("Negative Test for changePassword method of UserService.")
	void testChangePasswordForInvalidUsernameAndPasswordException() throws ResourceNotFoundException {

		Optional<User> userOptional = Optional.ofNullable(user);

		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);

		UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(user.getEmail(), "wrongPassword@123",
				"NewPassword@123");

		assertThrows(InvalidUsernamOrPasswordException.class, () -> userService.changePassword(userChangePasswordDto));

		verify(userRepository, times(1)).findByEmail(user.getEmail());

	}

	@Test
	@DisplayName("Test for getUserByEmail method of UserService.")
	void testGetUserByEmail() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(user);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName(user.getFullName())
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		UserDto returnedUserDto = userService.getUserByEmail("aquib@gmail.com");

		assertEquals(expectedUserDto, returnedUserDto);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
	}

	@Test
	@DisplayName("Negative Test for getUserByEmail method of UserService.")
	void testGetUserByEmailForResourceNotFoundException() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName(user.getFullName())
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail(user.getEmail()));

		verify(userRepository, times(1)).findByEmail(user.getEmail());
	}

	@Test
	@DisplayName("Test for updateProfile method of UserService.")
	void testUpdateProfile() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(user);
		when(userRepository.findById(1L)).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName("Aquib")
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		userDto.setFullName("Aquib");

		UserDto returnedUserDto = userService.updateProfile(userDto);

		assertEquals(expectedUserDto, returnedUserDto);

		verify(userRepository, times(1)).findById(user.getId());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("Negative Test for updateProfile method of UserService.")
	void testUpdateProfileForResourceNotFoundException() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findById(1L)).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName("Aquib")
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		userDto.setFullName("Aquib");

		assertThrows(ResourceNotFoundException.class, () -> userService.updateProfile(userDto));

		verify(userRepository, times(1)).findById(user.getId());
	}

	@Test
	@DisplayName("Test for userLogOut method of UserService.")
	void testUserLogOut() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(user);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName("Aquib")
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		Optional<UserLog> userLogOptional = Optional.ofNullable(userLog);
		when(userLogRepository.findUserLogByUser(user)).thenReturn(userLogOptional);

		String returnedMessage = userService.userLogOut(user.getEmail());

		assertEquals("Logged out successfully", returnedMessage);

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userLogRepository, times(1)).findUserLogByUser(user);
	}

	@Test
	@DisplayName("Negative Test for userLogOut method of UserService.")
	void testUserLogOutForResourceNotFoundExceptionWhenUserIsNull() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName("Aquib")
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		Optional<UserLog> userLogOptional = Optional.ofNullable(userLog);
		when(userLogRepository.findUserLogByUser(user)).thenReturn(userLogOptional);

		assertThrows(ResourceNotFoundException.class, () -> userService.userLogOut(user.getEmail()));

		verify(userRepository, times(1)).findByEmail(user.getEmail());
	}

	@Test
	@DisplayName("Negative Test for userLogOut method of UserService.")
	void testUserLogOutForResourceNotFoundExceptionWhenUserLogIsNull() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(user);
		when(userRepository.findByEmail("aquib@gmail.com")).thenReturn(userOptional);
		UserDto expectedUserDto = UserDto.builder().id(user.getId()).fullName("Aquib")
				.mobileNumber(user.getMobileNumber()).email(user.getEmail()).username(user.getUsername()).build();

		Optional<UserLog> userLogOptional = Optional.ofNullable(null);
		when(userLogRepository.findUserLogByUser(user)).thenReturn(userLogOptional);

		assertThrows(ResourceNotFoundException.class, () -> userService.userLogOut(user.getEmail()));

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		verify(userLogRepository, times(1)).findUserLogByUser(user);
	}

	@Test
	@DisplayName("Test for checkUserExistOrNot method of UserService.")
	void testUserExistOrNot() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(user);
		when(userRepository.findById(any(Long.class))).thenReturn(userOptional);
		assertTrue(userService.checkUserExistedOrNot(userDto.getId()));
	}

	@Test
	@DisplayName("Test for checkUserExistOrNot method of UserService.")
	void falsetestUserExistOrNot() throws ResourceNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findById(any(Long.class))).thenReturn(userOptional);
		assertFalse(userService.checkUserExistedOrNot(userDto.getId()));
	}

}
