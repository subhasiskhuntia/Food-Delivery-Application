package com.food.adminservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
	@Email(message = "Please enter a valid email address")
	@NotBlank(message = "email can not be blank")
	private String email;
	@NotBlank(message = "password can not be blank")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "Password must contain one capital letter,one small letter , one number and one special character and of at-least 8 character long")
	private String password;
}
