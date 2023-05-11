package com.food.restaurantservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantAddressDto {
    private long id;
    @NotBlank(message = "city can not be blank")
    private String city;
    @NotBlank(message = "state can not be blank")
    private String state;
    @NotBlank(message = "pincode can not be blank")
    private int pincode;
    @NotBlank(message = "houseNumber can not be blank")
    private String houseNumber;
    @NotBlank(message = "locality can not be blank")
    private String locality;
    @NotNull(message = "latitude can not be blank")
    private double latitude;
    @NotNull(message = "longitude can not be blank")
    private double longitude;
}
