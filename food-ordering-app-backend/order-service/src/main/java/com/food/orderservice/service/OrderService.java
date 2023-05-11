package com.food.orderservice.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.exception.ResourceNotFoundException;
import com.food.orderservice.model.TransactionDetails;

public interface OrderService {

	OrderDto placeOrder(OrderDto orderDto) throws ResourceNotFoundException, JsonProcessingException;

	String deleteOrder(Long id);

	OrderDto updateOrderStatus(String status, Long id) throws ResourceNotFoundException;

	List<Map<String, Object>> getUserOrders(Long userId) throws ResourceNotFoundException;

	TransactionDetails createTransaction(Double amount);

	OrderDto getOrderById(Long orderId) throws ResourceNotFoundException;

	List<Map<String, Object>> getOrdersOfARestaurants(Long restaurantId) throws ResourceNotFoundException;
}
