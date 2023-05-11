package com.food.restaurantservice.utils;

import java.time.LocalTime;

import com.food.restaurantservice.dto.RestaurantAddressDto;
import com.food.restaurantservice.dto.RestaurantDto;
import com.food.restaurantservice.model.Restaurant;
import com.food.restaurantservice.model.RestaurantAddress;

public class AppUtils {
    public static RestaurantDto mapRestaurantToRestaurantDto(Restaurant restaurant, String email){
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .userEmail(email)
                .name(restaurant.getName())
                .mobileNumber(restaurant.getMobileNumber())
                .description(restaurant.getDescription())
                .image(restaurant.getImage()).approvalStatus(restaurant.isApprovalStatus())
                .openedAt(restaurant.getOpenedOn().toString())
                .closedAt(restaurant.getClosedOn().toString())
                .address(
                        RestaurantAddressDto.builder()
                                .city(restaurant.getAddress().getCity())
                                .houseNumber(restaurant.getAddress().getHouseNumber())
                                .locality(restaurant.getAddress().getLocality())
                                .state(restaurant.getAddress().getState())
                                .pincode(restaurant.getAddress().getPincode())
                                .latitude(restaurant.getAddress().getLatitude())
                                .longitude(restaurant.getAddress().getLongitude())
                                .build()
                )
                .build();
    }
    public static Restaurant mapRestaurantDtoToRestaurant(RestaurantDto restaurantDto,Long id){
        return Restaurant.builder()
                .userId(id)
                .name(restaurantDto.getName())
                .mobileNumber(restaurantDto.getMobileNumber())
                .description(restaurantDto.getDescription())
                .openedOn(LocalTime.parse(restaurantDto.getOpenedAt()))
                .closedOn(LocalTime.parse(restaurantDto.getClosedAt()))
                .image(restaurantDto.getImage())
                .address(
                        RestaurantAddress.builder()
                                .city(restaurantDto.getAddress().getCity())
                                .houseNumber(restaurantDto.getAddress().getHouseNumber())
                                .locality(restaurantDto.getAddress().getLocality())
                                .state(restaurantDto.getAddress().getState())
                                .pincode(restaurantDto.getAddress().getPincode())
                                .latitude(restaurantDto.getAddress().getLatitude())
                                .longitude(restaurantDto.getAddress().getLongitude())
                                .build()
                )
                .build();
    }
    
    public static RestaurantDto mapRestaurantToRestaurantDtoWithoutEmail(Restaurant restaurant){
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .mobileNumber(restaurant.getMobileNumber())
                .description(restaurant.getDescription())
				.image(restaurant.getImage()).approvalStatus(restaurant.isApprovalStatus())
                .openedAt(restaurant.getOpenedOn().toString())
                .closedAt(restaurant.getClosedOn().toString())
                .address(
                        RestaurantAddressDto.builder()
                                .city(restaurant.getAddress().getCity())
                                .houseNumber(restaurant.getAddress().getHouseNumber())
                                .locality(restaurant.getAddress().getLocality())
                                .state(restaurant.getAddress().getState())
                                .pincode(restaurant.getAddress().getPincode())
                                .latitude(restaurant.getAddress().getLatitude())
                                .longitude(restaurant.getAddress().getLongitude())
                                .build()
                )
                .build();
    }
}
