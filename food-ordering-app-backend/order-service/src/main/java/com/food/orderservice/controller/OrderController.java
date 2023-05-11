package com.food.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.exception.ResourceNotFoundException;
import com.food.orderservice.model.TransactionDetails;
import com.food.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping(value = "/placeOrder")
	public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderDto orderDto)
			throws ResourceNotFoundException, JsonProcessingException {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderDto));
	}

	@DeleteMapping(value = "/cancelOrder")
	public ResponseEntity<String> cancelOrder(@RequestParam("id") Long id) {
		return ResponseEntity.ok(orderService.deleteOrder(id));
	}

	@PatchMapping(value = "/updateOrderStatus")
	public ResponseEntity<OrderDto> updateOrderStatus(@RequestParam("id") Long id,
			@RequestParam("status") String status) throws ResourceNotFoundException {
		return ResponseEntity.ok(orderService.updateOrderStatus(status, id));
	}

	@GetMapping(value = "/getUsersOrder")
	public ResponseEntity<List<Map<String, Object>>> getUserOrder(@RequestParam("userId") Long userId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(orderService.getUserOrders(userId));
	}

	@GetMapping(value = "/getOrderById")
	public ResponseEntity<OrderDto> getOrderById(@RequestParam("orderId") Long orderId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(orderService.getOrderById(orderId));
	}

	@GetMapping("/createTransaction/{amount}")
	public ResponseEntity<TransactionDetails> createTransaction(@PathVariable("amount") Double amount) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.createTransaction(amount));
	}

	@GetMapping(value = "/getOrdersOfARestaurants")
	public ResponseEntity<List<Map<String, Object>>> getOrdersOfARestaurants(
			@RequestParam("restaurantId") Long restaurantId) throws ResourceNotFoundException {
		return ResponseEntity.ok(orderService.getOrdersOfARestaurants(restaurantId));
	}
}