package com.food.orderservice.dto;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    private Long id;
    @NotNull(message = "foodItemId can not be blank")
    private Long foodItemId;
    @NotNull(message = "restaurantId can not be blank")
    private Long restaurantId;
    @Min(value = 1, message = "price can't be less than 1")
    private int quantity;
}


