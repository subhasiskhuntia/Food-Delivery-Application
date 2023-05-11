package com.food.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.food.cartservice.dto.CartDto;
import com.food.cartservice.exception.CartForAnotherRestaurantAlreadyExistsException;
import com.food.cartservice.exception.ResourceNotFoundException;
import com.food.cartservice.service.CartService;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    

    @PostMapping(value = "/addToCart")
    public ResponseEntity<CartDto> addCartItem(@RequestBody @Valid CartDto cartDto) throws ResourceNotFoundException, CartForAnotherRestaurantAlreadyExistsException {
        return ResponseEntity.ok(cartService.addCartItem(cartDto));
    }
    @PatchMapping(value = "/updateCartItem")
    public ResponseEntity<CartDto> updateCartItem(@RequestBody @Valid CartDto cartDto) throws ResourceNotFoundException{
        return ResponseEntity.ok(cartService.updateCartItem(cartDto));
    }
    @DeleteMapping(value = "/deleteCartItem")
    public ResponseEntity<String> deleteCartItme(@RequestParam("cartId") Long cartId) throws ResourceNotFoundException{
        return ResponseEntity.ok(cartService.deleteCartItem(cartId));
    }
    @GetMapping(value = "/getCartItemsByUserId")
    public ResponseEntity<List<CartDto>> getCartItemsByUserId(@RequestParam("userId") Long userId) throws  ResourceNotFoundException{
        return ResponseEntity.ok(cartService.getCartItemsOfAUser(userId));
    }
    
    @DeleteMapping(value = "/deleteCartItemsByUserId")
    public ResponseEntity<String> deleteCartItemsByUserId(@RequestParam("userId") Long userId) throws ResourceNotFoundException{
        return ResponseEntity.ok(cartService.deleteCartItemsByUserId(userId));
    }
}