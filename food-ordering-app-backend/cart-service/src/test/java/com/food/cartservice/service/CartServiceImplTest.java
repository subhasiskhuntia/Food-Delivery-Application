package com.food.cartservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.food.cartservice.dto.CartDto;
import com.food.cartservice.exception.ResourceNotFoundException;
import com.food.cartservice.feign.FoodProxyService;
import com.food.cartservice.feign.UserProxyService;
import com.food.cartservice.model.Cart;
import com.food.cartservice.repository.CartRepository;

class CartServiceImplTest {
	
	@Mock
	private CartRepository cartRepository;
	@Mock
	private UserProxyService userProxyService;
	@Mock
	private FoodProxyService foodProxyService;
	private CartService cartService;
	
	Cart cart;
	CartDto cartDto;
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		cartService=new CartServiceImpl(cartRepository,userProxyService,foodProxyService);
		cart=new Cart();
		cart=Cart.builder().id(1l).userId(1l).foodItemId(1l).quantity(2).build();
		
		cartDto=new CartDto();
		cartDto=CartDto.builder().id(1l).foodItemId(1l).userId(1l).quantity(1l).build();
	}

	@Test
	@DisplayName("test addCartItem method when user is not present of CartServiceImpl class")
	void testAddCartItemWheUserNotExist() {
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		assertThrows(ResourceNotFoundException.class, ()->cartService.addCartItem(cartDto));
	}
	@Test
	@DisplayName("test addCartItem method when food is not present of CartServiceImpl class")
	void testAddCartItemWhenFoodNotExist() {
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodProxyService.foodItemExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		assertThrows(ResourceNotFoundException.class, ()->cartService.addCartItem(cartDto));
	}
	@Test
	@DisplayName("test addCartItem method of CartServiceImpl class")
	void testAddCartItem()throws Exception {
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodProxyService.foodItemExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		assertNotNull(cartService.addCartItem(cartDto));
	}

	@Test
	@DisplayName("test addCartItem method of CartServiceImpl class")
	void testUpdateCartItemNoCartIdPresent() {
		cartDto.setId(null);
		assertThrows(ResourceNotFoundException.class,()->cartService.updateCartItem(cartDto));
	}
	@Test
	@DisplayName("test addCartItem method of CartServiceImpl class")
	void testUpdateCartItemNoCartPresent() {
		Optional<Cart> cartOptional=Optional.ofNullable(null);
		when(cartRepository.findById(any(Long.class))).thenReturn(cartOptional);
		assertThrows(ResourceNotFoundException.class,()->cartService.updateCartItem(cartDto));
	}
	@Test
	@DisplayName("test addCartItem method of CartServiceImpl class")
	void testUpdateCartItem()throws Exception {
		Optional<Cart> cartOptional=Optional.ofNullable(cart);
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		when(cartRepository.findById(any(Long.class))).thenReturn(cartOptional);
		assertNotNull(cartService.updateCartItem(cartDto));
	}

	@Test
	@DisplayName("test deleteCartItem method when id is not present of CartServiceImpl class")
	void testDeleteCartItemwhenIdNotPresent() {
		cartDto.setId(null);
		assertThrows(ResourceNotFoundException.class,()->cartService.deleteCartItem(null));
	}
	@Test
	@DisplayName("test deleteCartItem method when cart is not present of CartServiceImpl class")
	void testDeleteCartItemwhenCartNotPresent() {
		Optional<Cart> cartOptional=Optional.ofNullable(null);
		when(cartRepository.findById(any(Long.class))).thenReturn(cartOptional);
		assertThrows(ResourceNotFoundException.class,()->cartService.deleteCartItem(1L));
	}
	@Test
	@DisplayName("test deleteCartItem method  of CartServiceImpl class")
	void testDeleteCartItemwhen()throws Exception {
		Optional<Cart> cartOptional=Optional.ofNullable(cart);
		when(cartRepository.findById(any(Long.class))).thenReturn(cartOptional);
		doNothing().when(cartRepository).deleteById(any(Long.class));
		assertEquals("Cart Item deleted successfully",cartService.deleteCartItem(1L));
	}

	@Test
	@DisplayName("test getAllCartItemsOfAIUser when user not found of class CartServiceImpl")
	void testGetCartItemsOfAUserWhenUserNotFound() {
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		assertThrows(ResourceNotFoundException.class, ()->cartService.getCartItemsOfAUser(cartDto.getId()));
	}
	@Test
	@DisplayName("test getAllCartItemsOfAIUser  of class CartServiceImpl")
	void testGetCartItemsOfAUser()throws Exception {
		List<Cart> cartItems=new ArrayList<Cart>();
		cartItems.add(cart);
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(cartRepository.findCartByUserId(any(Long.class))).thenReturn(cartItems);
		assertNotNull(cartService.getCartItemsOfAUser(cartDto.getId()));
	}


}
