package com.food.userservice.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {
	@NotBlank(message = "Email can not be Blank")
	private String email;
	@NotBlank(message = "Password can not be Blank")
	private String password;

}
