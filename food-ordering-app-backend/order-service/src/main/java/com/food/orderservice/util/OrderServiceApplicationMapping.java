package com.food.orderservice.util;

import java.util.stream.Collectors;

import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.dto.OrderItemDto;
import com.food.orderservice.model.OrderItem;
import com.food.orderservice.model.UserOrder;

public class OrderServiceApplicationMapping {
	public static UserOrder mapOrderDtoToOrder(OrderDto orderDto) {
		return UserOrder.builder().id(orderDto.getId()).price(orderDto.getPrice())
				.restaurantId(orderDto.getRestaurantId())
				.transactionId(orderDto.getTransactionId()).userAddressId(orderDto.getUserAddressId())
				.orderStatus(orderDto.getOrderStatus()).userId(orderDto.getUserId())
				.orderItems(orderDto.getOrderItems().stream()
						.map(orderItemDto -> mapOrderItemDtoToOrderItem(orderItemDto)).collect(Collectors.toList())

				).build();
	}

	public static OrderDto mapOrderToOrderDto(UserOrder order) {

		return OrderDto.builder().id(order.getId()).price(order.getPrice()).
				restaurantId(order.getRestaurantId())
				.transactionId(order.getTransactionId())
				.userAddressId(order.getUserAddressId()).userId(order.getUserId()).orderedOn(order.getOrderedOn().toString())
				.orderStatus(order.getOrderStatus()).orderItems(order.getOrderItems().stream()
						.map(orderItem -> mapOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList()))
				.build();
	}

	public static OrderItemDto mapOrderItemToOrderItemDto(OrderItem orderItem) {
		return OrderItemDto.builder().id(orderItem.getId()).foodItemId(orderItem.getFoodItemId())
				.quantity(orderItem.getQuantity()).build();
	}

	public static OrderItem mapOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
		return OrderItem.builder().foodItemId(orderItemDto.getFoodItemId()).id(orderItemDto.getId())
				.quantity(orderItemDto.getQuantity()).build();
	}

}
