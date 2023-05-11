package com.food.cartservice.util;

import com.food.cartservice.dto.CartDto;
import com.food.cartservice.model.Cart;

public class CartServiceMapping {
    public static Cart mapCartDtoToCart(CartDto cartDto){
        return Cart.builder()
                .id(cartDto.getId())
                .foodItemId(cartDto.getFoodItemId())
                .userId(cartDto.getUserId())
                .quantity(cartDto.getQuantity())
                .restaurantId(cartDto.getRestaurantId())
                .build();
    }
    public static CartDto mapCartToCartDto(Cart cart){
        return CartDto.builder()
                .id(cart.getId())
                .foodItemId(cart.getFoodItemId())
//                .userId(cart.getUserId())
                .quantity(cart.getQuantity())
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .build();
    }
}

