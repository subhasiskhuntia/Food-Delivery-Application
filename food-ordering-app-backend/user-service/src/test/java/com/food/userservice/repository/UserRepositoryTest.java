package com.food.userservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.food.userservice.model.User;

@SpringBootTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		user = new User();

		user.setFullName("Aquib Azhar");
		user.setMobileNumber("9029182812");
		user.setEmail("aquib@gmail.com");
		user.setUsername("aquibazhar");
		user.setPassword("Aquib@123");
		
		userRepository.save(user);
	}

	@AfterEach
	public void tearDown() {
		user = null;
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("Test for findByEmailAndPassword custom method of UserRepository.")
	void testFindByEmailAndPassword() {


		Optional<User> userOptional = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		User actualUser = userOptional.get();

		assertEquals(user.getEmail(), actualUser.getEmail());
		assertEquals(user.getFullName(), actualUser.getFullName());
		assertEquals(user.getMobileNumber(), actualUser.getMobileNumber());
		assertEquals(user.getUsername(), actualUser.getUsername());
		assertEquals(user.getPassword(), actualUser.getPassword());

	}

	@Test
	@DisplayName("Test for findByEmail custom method of UserRepository.")
	void testFindByEmail() {


		Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
		User actualUser = userOptional.get();
		
		assertEquals(user.getEmail(), actualUser.getEmail());
		assertEquals(user.getFullName(), actualUser.getFullName());
		assertEquals(user.getMobileNumber(), actualUser.getMobileNumber());
		assertEquals(user.getUsername(), actualUser.getUsername());
		assertEquals(user.getPassword(), actualUser.getPassword());
	}

	@Test
	@DisplayName("Test for findByPassword custom method of UserRepository.")
	void testFindByPassword() {


		Optional<User> userOptional = userRepository.findByPassword(user.getPassword());
		User actualUser = userOptional.get();
		
		assertEquals(user.getEmail(), actualUser.getEmail());
		assertEquals(user.getFullName(), actualUser.getFullName());
		assertEquals(user.getMobileNumber(), actualUser.getMobileNumber());
		assertEquals(user.getUsername(), actualUser.getUsername());
		assertEquals(user.getPassword(), actualUser.getPassword());
	}

}
