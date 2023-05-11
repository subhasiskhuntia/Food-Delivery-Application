package com.food.foodItemservice.dto;

import javax.validation.constraints.NotNull;

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
public class FoodItemDto {
	
	private Long id;
	@NotNull(message = "Price can not be empty")
	private float price;
	private String description;
	@NotNull(message = "restaurantId can not be empty")
	private Long restaurantId;
	@NotNull(message = "Food details can not be empty")
	private FoodDto food;
	private FoodImageDto image;
}
