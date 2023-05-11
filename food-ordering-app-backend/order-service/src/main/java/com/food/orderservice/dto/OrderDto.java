package com.food.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private Long id;
    private String orderedOn;
    @Min(value = 1,message = "Price must be greater than 0")
    private double price;
    @NotNull(message = "userId can not be blank")
    private Long userId;
    @NotNull(message = "userAddress can not be blank")
    private Long userAddressId;
    @NotBlank(message = "transactionId can not be blank")
    private String transactionId;
    private String orderStatus;
    private Long restaurantId;
    @NotNull(message = "No item found")
    private List<OrderItemDto> orderItems;

}

