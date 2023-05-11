package com.food.foodItemservice.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodDto {
	private Long id;
	@NotBlank(message = "Food-name can not be empty")
	private String foodName;
	@NotBlank(message = "cuisine type can not be empty")
	private String cuisine;
	@NotBlank(message = "mealType can not be blank")
	private String mealType;
}
