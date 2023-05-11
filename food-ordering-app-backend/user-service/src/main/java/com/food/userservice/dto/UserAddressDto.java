package com.food.userservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDto {
	private long id;
	@NotBlank(message = "user not found")
	private String email;
	@NotBlank(message = "city name can not be empty")
	private String city;
	@NotBlank(message = "state name can not be empty")
	private String state;
	@NotNull(message = "pincode can not be empty")
	private int pincode;
	@NotBlank(message = "house number can not be empty")
	private String houseNumber;
	@NotBlank(message = "locality can not be empty")
	private String locality;

}
