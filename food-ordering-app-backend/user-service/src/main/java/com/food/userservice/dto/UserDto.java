package com.food.userservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private long id;
	@Email(message = "Please enter a valid email address")
	@NotBlank(message = "email can not be blank")
	private String email;
	@NotBlank(message = "username can not be blank")
	private String username;
	@NotBlank(message = "fullName can not be blank")
	private String fullName;
	@Size(min = 10, max = 15, message = "mobile number can not be smaller than 10 character")
	private String mobileNumber;
	@NotBlank(message = "password can not be blank")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "Password must contain one capital letter,one small letter , one number and one special character and of at-least 8 character long")
	private String password;
	
	private String role;

}
