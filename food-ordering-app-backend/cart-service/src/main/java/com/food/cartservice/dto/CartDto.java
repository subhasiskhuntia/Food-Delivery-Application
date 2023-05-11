package com.food.cartservice.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    @NotNull(message = "userId can not be empty")
    private Long userId;
    @NotNull(message = "foodItemId can not be empty")
    private Long foodItemId;
    @Min(value = 1,message = "quantity can not be less than 1")
    private long quantity;
    private Long restaurantId;
}


