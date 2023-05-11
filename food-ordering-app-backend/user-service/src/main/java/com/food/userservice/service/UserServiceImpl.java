package com.food.userservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.userservice.dto.UserDto;
import com.food.userservice.exception.InvalidUsernamOrPasswordException;
import com.food.userservice.exception.ResourceNotFoundException;
import com.food.userservice.model.User;
import com.food.userservice.model.UserLog;
import com.food.userservice.repository.UserLogRepository;
import com.food.userservice.repository.UserRepository;
import com.food.userservice.util.UserServiceMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.food.userservice.dto.UserChangePasswordDto;
import com.food.userservice.dto.UserCredential;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	public final UserRepository userRepository;
	public final UserLogRepository userLogRepository;

	@Override
	public User createUser(UserDto userDto) {
		User user = User.builder().email(userDto.getEmail()).fullName(userDto.getFullName())
				.mobileNumber(userDto.getMobileNumber()).password(userDto.getPassword()).username(userDto.getUsername())
				.build();

		return userRepository.save(user);
	}

	@Override
	@Transactional
	public String validateUser(UserCredential userCredential)
			throws InvalidUsernamOrPasswordException, ResourceNotFoundException {
		User user = userRepository.findByEmailAndPassword(userCredential.getEmail(), userCredential.getPassword())
				.orElseThrow(() -> new InvalidUsernamOrPasswordException("Invalid Email or Password"));

		UserLog userLog = userLogRepository.findUserLogByUser(user).orElse(null);

		if (userLog == null) {
			UserLog newUserLog = UserLog.builder().user(user).lastLoggedIn(LocalDateTime.now()).totalVisits(1)
					.userStatus("ACTIVE").build();
			userLogRepository.save(newUserLog);
		} else {
			userLog.setLastLoggedIn(LocalDateTime.now());
			userLog.setUserStatus("Active");
			userLog.setTotalVisits(userLog.getTotalVisits() + 1);
			userLogRepository.save(userLog);
		}
		return "User logged in successfully";
	}

	@Override
	public UserDto updateProfile(UserDto userDto) throws ResourceNotFoundException {
		User user = userRepository.findById(userDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User with this id doesn't exist"));

		user.setFullName(userDto.getFullName());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());

		userRepository.save(user);
		log.info("user having id {} updated successfully" + user.getId());
		return UserDto.builder().id(user.getId()).fullName(user.getFullName()).mobileNumber(user.getMobileNumber())
				.email(user.getEmail()).username(user.getUsername()).build();
	}

	@Override
	public String userLogOut(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User with this email doesn't exist " + email));

		UserLog userLog = userLogRepository.findUserLogByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Logs for this user doesn't exist."));
		userLog.setLastLoggedOut(LocalDateTime.now());
		userLog.setUserStatus("In-Active");
		userLogRepository.save(userLog);
		return "Logged out successfully";
	}

	@Override
	public String changePassword(UserChangePasswordDto changePasswordDto)
			throws ResourceNotFoundException, InvalidUsernamOrPasswordException {
		User user = userRepository.findByEmail(changePasswordDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User with this enail doesn't exist."));

		if (changePasswordDto.getOldPassword().equals(user.getPassword())) {
			user.setPassword(changePasswordDto.getNewPassword());
			userRepository.save(user);
			return "Password changed successfully";
		}
		throw new InvalidUsernamOrPasswordException("Invalid Password.");

	}

	@Override
	public UserDto getUserByEmail(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email));

		return UserDto.builder().id(user.getId()).fullName(user.getFullName()).mobileNumber(user.getMobileNumber())
				.email(user.getEmail()).username(user.getUsername()).role(user.getRole()).build();
	}

	@Override
	public Boolean checkUserExistedOrNot(long id) {
		Optional<User> user = userRepository.findById(id);
		return user.isPresent();
	}

	@Override
	public String updateUserRole(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User with this enail doesn't exist."));

		user.setRole("restaurant");
		userRepository.save(user);
		return "User role updated to restaurant";
	}

	@Override
	public UserDto getByUserId(long userId) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with this ID doesn't exist " + userId));

		return UserDto.builder().id(user.getId()).fullName(user.getFullName()).mobileNumber(user.getMobileNumber())
				.email(user.getEmail()).username(user.getUsername()).role(user.getRole()).build();
	}

	@Override
	public List<UserDto> getMultipleUsersByIds(List<Long> userIds) {
		return userRepository.findAllById(userIds).stream().map(user -> UserServiceMapping.mapUserToUserDto(user))
				.collect(Collectors.toList());
	}
}
