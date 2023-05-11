package com.food.restaurantservice.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDto {
    private long id;
    @NotBlank(message = "restauant name can not be blank")
    private String name;
    @NotBlank(message = "mobile number can not be blank")
    private String mobileNumber;
    @NotBlank(message = "description can not be blank")
    private String description;
    private boolean approvalStatus;
    @NotBlank(message = "user email can not blank")
    private String userEmail;
    @NotBlank(message = "opening time can not be blank")
    private String openedAt;
    @NotBlank(message = "closing time can not be blank")
    private String closedAt;
    @NotNull(message = "user address can not blank")
    private RestaurantAddressDto address;
    @Lob
    private String image;

}
