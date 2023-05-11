package com.food.cartservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.food.cartservice.dto.CartDto;
import com.food.cartservice.exception.CartForAnotherRestaurantAlreadyExistsException;
import com.food.cartservice.exception.ResourceNotFoundException;
import com.food.cartservice.feign.FoodProxyService;
import com.food.cartservice.feign.UserProxyService;
import com.food.cartservice.model.Cart;
import com.food.cartservice.repository.CartRepository;
import com.food.cartservice.util.CartServiceMapping;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final UserProxyService userProxyService;
	private final FoodProxyService foodProxyService;

	@Override
	@CircuitBreaker(fallbackMethod = "addCartItemFallback", name = "fallBackForAddCartItem")
	public CartDto addCartItem(CartDto cartDto)
			throws ResourceNotFoundException, CartForAnotherRestaurantAlreadyExistsException {
		Boolean userExisted = userProxyService.userExistOrNot(cartDto.getUserId()).getBody();

		if (!userExisted)
			throw new ResourceNotFoundException("User not found having id " + cartDto.getUserId());
		try {
			Map<String, Object> foodItemDtoMap = foodProxyService.getFoodItemById(cartDto.getFoodItemId()).getBody();
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("FoodItem not found having id: " + cartDto.getFoodItemId());
		}
		Map<String, Object> foodItemDtoMap = foodProxyService.getFoodItemById(cartDto.getFoodItemId()).getBody();
		if (!foodItemDtoMap.containsKey("id"))
			throw new ResourceNotFoundException("FoodItem does not exist");
		List<Cart> cartItems = cartRepository.findCartByUserId(cartDto.getUserId());
		cartDto.setRestaurantId(Long.parseLong(foodItemDtoMap.get("restaurantId").toString()));

		if (cartItems.stream().anyMatch((cart) -> cart.getRestaurantId() != cartDto.getRestaurantId())) {
			throw new CartForAnotherRestaurantAlreadyExistsException(
					"Can not have foodItems of multiple restaurants at a time in the cart.");
		}

		return CartServiceMapping.mapCartToCartDto(cartRepository.save(CartServiceMapping.mapCartDtoToCart(cartDto)));

	}

	public CartDto addCartItemFallback(CartDto cartDto, RuntimeException exception)
			throws ResourceNotFoundException, CartForAnotherRestaurantAlreadyExistsException {
		return CartDto.builder().id(0L).foodItemId(0L).restaurantId(0L).quantity(0).userId(0L).build();
	}

	@Override
	public CartDto updateCartItem(CartDto cartDto) throws ResourceNotFoundException {
		if (cartDto.getId() == null)
			throw new ResourceNotFoundException("No CartItem found");
		Cart cart = cartRepository.findById(cartDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));
		cart.setQuantity(cartDto.getQuantity());
		return CartServiceMapping.mapCartToCartDto(cartRepository.save(cart));
	}

	@Override

	public String deleteCartItem(Long cartId) throws ResourceNotFoundException {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
		cartRepository.deleteById(cart.getId());
		return "Cart Item deleted successfully";
	}

	@Override
	@CircuitBreaker(fallbackMethod = "getCartItemsOfAUserFallback", name = "fallBackForGetCartItemsOfAUser")
	public List<CartDto> getCartItemsOfAUser(Long id) throws ResourceNotFoundException {
		Boolean userExisted = userProxyService.userExistOrNot(id).getBody();
		System.out.println(userExisted);
		if (!userExisted)
			throw new ResourceNotFoundException("User not found having id " + id);

		List<Cart> cartItems = cartRepository.findCartByUserId(id);
		return cartItems.stream().map(cart -> CartServiceMapping.mapCartToCartDto(cart)).collect(Collectors.toList());
	}

	public List<CartDto> getCartItemsOfAUserFallback(Long id, RuntimeException exception)
			throws ResourceNotFoundException {
		CartDto defaultCartDto = CartDto.builder().id(0L).foodItemId(0L).restaurantId(0L).quantity(0).userId(0L)
				.build();
		List<CartDto> defaultCartDtoList = new ArrayList<>();
		defaultCartDtoList.add(defaultCartDto);
		return defaultCartDtoList;
	}

	@Override
	@CircuitBreaker(fallbackMethod = "deleteCartItemsByUserIdFallback", name = "fallBackForDeleteCartItemsByUserId")
	public String deleteCartItemsByUserId(Long userId) throws ResourceNotFoundException {
		Boolean userExisted = userProxyService.userExistOrNot(userId).getBody();
		System.out.println(userExisted);
		if (!userExisted)
			throw new ResourceNotFoundException("User not found having id " + userId);

		cartRepository.deleteByUserId(userId);
		return "Cart Items deleted successfully";
	}

	public String deleteCartItemsByUserIdFallback(Long userId, RuntimeException exception)
			throws ResourceNotFoundException {
		return "User Service is currently down. Please try again sometime.";
	}

}
