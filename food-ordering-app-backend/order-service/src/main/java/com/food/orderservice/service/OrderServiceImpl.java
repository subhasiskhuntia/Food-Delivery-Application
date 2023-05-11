package com.food.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.dto.OrderItemDto;
import com.food.orderservice.exception.ResourceNotFoundException;
import com.food.orderservice.feign.CartProxyService;
import com.food.orderservice.feign.FoodItemProxyService;
import com.food.orderservice.feign.RestaurantProxyService;
import com.food.orderservice.feign.UserProxyService;
import com.food.orderservice.model.OrderItem;
import com.food.orderservice.model.TransactionDetails;
import com.food.orderservice.model.UserOrder;
import com.food.orderservice.repository.OrderItemRepository;
import com.food.orderservice.repository.OrderRepository;
import com.food.orderservice.util.OrderServiceApplicationMapping;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final UserProxyService userProxyService;
	private final FoodItemProxyService foodItemProxyService;
	private final RestaurantProxyService restaurantProxyService;

	@Autowired
	private CartProxyService cartProxyService;

	@Value("${KEY}")
	private static String KEY;
	@Value("${KEY_SECRET}")
	private static String KEY_SECRET;
	@Value("${CURRENCY}")
	private static String CURRENCY;

	@Override
	@Transactional
	@CircuitBreaker(fallbackMethod = "placeOrderFallback", name = "fallBackForPlaceOrder")
	public OrderDto placeOrder(OrderDto orderDto) throws ResourceNotFoundException, JsonProcessingException {
		boolean userExist = userProxyService.userExistOrNot(orderDto.getUserId()).getBody();
		if (!userExist)
			throw new ResourceNotFoundException("User not found");
		boolean userAddressExist = userProxyService.addressExistByIdOrNot(orderDto.getUserAddressId()).getBody();
		if (!userAddressExist)
			throw new ResourceNotFoundException("User address not found");
		List<Long> ids = orderDto.getOrderItems().stream().map(orderItemDto -> orderItemDto.getFoodItemId())
				.collect(Collectors.toList());

		List<Map<String, Object>> foodItemsObject = foodItemProxyService.getMultipleFoodItemByIds(ids).getBody();
		if (foodItemsObject.size() != ids.size()) {
			throw new ResourceNotFoundException("Some FoodItem does not exist");
		}
		for (OrderItemDto item : orderDto.getOrderItems()) {
			for (Map<String, Object> map : foodItemsObject) {
				Long tempFoodItemId = Long.parseLong(map.get("id").toString());
				if (item.getFoodItemId().equals(tempFoodItemId)) {
					item.setRestaurantId(Long.parseLong(map.get("restaurantId").toString()));
				}
			}
		}
		orderDto.setRestaurantId(Long.parseLong(foodItemsObject.get(0).get("restaurantId").toString()));
		for (OrderItemDto item : orderDto.getOrderItems()) {
			if (item.getRestaurantId() != orderDto.getRestaurantId())
				throw new ResourceNotFoundException("Can not place order from multiple restaurants");
		}
		UserOrder requestedOrder = OrderServiceApplicationMapping.mapOrderDtoToOrder(orderDto);
		requestedOrder.setOrderStatus("Ordered");
		UserOrder savedOrder = orderRepository.save(requestedOrder);

		// checking if the fooditems are of one restaurant or not

		for (OrderItemDto item : orderDto.getOrderItems()) {
			for (Map<String, Object> map : foodItemsObject) {
				Long tempFoodItemId = Long.parseLong(map.get("id").toString());
				if (item.getFoodItemId() == tempFoodItemId) {
					item.setRestaurantId(Long.parseLong(map.get("restaurantId").toString()));
				}
			}
		}
		List<OrderItem> orderItems = savedOrder.getOrderItems().stream().map(orderItem -> {
			orderItem.setOrder(savedOrder);
			return orderItem;
		}).collect(Collectors.toList());
		orderItemRepository.saveAll(orderItems);

		cartProxyService.deleteCartItemsByUserId(orderDto.getUserId());

		return OrderServiceApplicationMapping.mapOrderToOrderDto(savedOrder);
	}

	public OrderDto placeOrderFallback(OrderDto orderDto, RuntimeException exception)
			throws ResourceNotFoundException, JsonProcessingException {
		OrderItemDto defaultOrderItemDto = OrderItemDto.builder().id(0L).foodItemId(0L).quantity(0).restaurantId(0L)
				.build();
		List<OrderItemDto> defaultOrderItemDtoList = new ArrayList<>();
		defaultOrderItemDtoList.add(defaultOrderItemDto);
		return OrderDto.builder().id(0L).orderedOn("10-01-2023").price(0).userId(0L).userAddressId(0L)
				.transactionId("0").orderStatus("Ordered").restaurantId(0L).orderItems(defaultOrderItemDtoList).build();
	}

	@Override
	public String deleteOrder(Long id) {
		orderRepository.deleteById(id);
		return "Order cancelled successfully";
	}

	@Override
	public OrderDto updateOrderStatus(String status, Long id) throws ResourceNotFoundException {
		UserOrder order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		order.setOrderStatus(status);
		orderRepository.save(order);
		return OrderServiceApplicationMapping.mapOrderToOrderDto(order);
	}

	@Override
	@CircuitBreaker(fallbackMethod = "getUserOrdersFallback", name = "fallBackForGetUserOrders")
	public List<Map<String, Object>> getUserOrders(Long userId) throws ResourceNotFoundException {
		List<UserOrder> orders = orderRepository.findUserOrderByUserId(userId);
		Map<String, Object> userDetails;
		if (orders.size() == 0)
			return Arrays.asList();
		try {
			userDetails = userProxyService.getByUserId(userId).getBody();
		} catch (FeignException.NotFound e) {
			// TODO Auto-generated catch block
			throw new ResourceNotFoundException("User not found having id: " + userId);
		}
		Set<Long> foodItemIds = orders.stream()
				.flatMap(order -> order.getOrderItems().stream().map(orderItem -> orderItem.getFoodItemId()))
				.collect(Collectors.toSet());
		System.out.println(foodItemIds);
		List<Map<String, Object>> foodItemDetails = foodItemProxyService
				.getMultipleFoodItemByIds(new ArrayList<>(foodItemIds)).getBody();

		Set<Long> restaurantIds = orders.stream().map(order -> order.getRestaurantId()).collect(Collectors.toSet());

		Set<Long> userAddressIds = orders.stream().map(order -> order.getUserAddressId()).collect(Collectors.toSet());
		List<Map<String, Object>> restaurantDetails = restaurantProxyService
				.getMultipleRestaurantByIds(new ArrayList<Long>(restaurantIds)).getBody();
		List<Map<String, Object>> userAddressDetails = userProxyService
				.getMultipleUserAddressIds(new ArrayList<Long>(userAddressIds)).getBody();
		System.out.println(userDetails);
		System.out.println(userAddressDetails);
//		System.out.println(foodItemDetails);
		return orders.stream().map(order -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", order.getId());
			map.put("transactionId", order.getTransactionId());
			map.put("status", order.getOrderStatus());
			map.put("orderedOn", order.getOrderedOn().toString());
			map.put("price", order.getPrice());
//					map.put("restaurantDetails", restaurantDetails);
			map.put("restaurantDetails",
					restaurantDetails
							.stream().filter(restaurantDetail -> Long
									.parseLong(restaurantDetail.get("id").toString()) == order.getRestaurantId())
							.findAny().orElse(null));
			map.put("orderItems", foodItemDetails.stream().map(singleFoodItemDetails -> {
				for (OrderItem orderItem : order.getOrderItems()) {
					Map<String, Object> orderItemMap = new HashMap<>();
					if (Long.parseLong(singleFoodItemDetails.get("id").toString()) == orderItem.getFoodItemId()) {
						orderItemMap.put("foodItem", singleFoodItemDetails);
						orderItemMap.put("quantity", orderItem.getQuantity());
						return orderItemMap;
					}
				}
				return null;

			}).filter(orderItem -> orderItem != null).collect(Collectors.toList()));
//					map.put("user", userDetails.stream().filter(userDetail->
//						Long.parseLong(userDetail.get("id").toString())==order.getUserId()
//					).findAny().orElse(null));
			map.put("userAddress", userAddressDetails.stream()
					.filter(userAddress -> Long.parseLong(userAddress.get("id").toString()) == order.getUserAddressId())
					.findAny().orElse(null));
			return map;
		}).collect(Collectors.toList());

	}

	public List<Map<String, Object>> getUserOrdersFallback(Long userId, RuntimeException exception)
			throws ResourceNotFoundException {
		Map<String, Object> defaultResponse = new HashMap<>();
		defaultResponse.put("Message", "Service is currently down. Please try again sometime.");
		List<Map<String, Object>> defaultResponseList = new ArrayList<>();
		defaultResponseList.add(defaultResponse);
		return defaultResponseList;
	}

	@Override
	public TransactionDetails createTransaction(Double amount) {
		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", amount * 100);
			jsonObject.put("currency", CURRENCY);

			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
			Order razorOrder = razorpayClient.orders.create(jsonObject);

			return this.prepareTransactionDetails(razorOrder);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public TransactionDetails prepareTransactionDetails(Order razorOrder) {
		String orderId = razorOrder.get("id");
		String currency = razorOrder.get("currency");
		Integer amount = razorOrder.get("amount");

		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setOrderId(orderId);
		transactionDetails.setCurrency(currency);
		transactionDetails.setAmount(amount);
		transactionDetails.setKey(KEY);
		return transactionDetails;
	}

	@Override
	public OrderDto getOrderById(Long orderId) throws ResourceNotFoundException {
		UserOrder order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order with this ID doesn't exist"));

		return OrderServiceApplicationMapping.mapOrderToOrderDto(order);
	}

	@Override
	@CircuitBreaker(fallbackMethod = "getOrdersOfARestaurantsFallback", name = "fallBackForGetOrdersOfARestaurants")
	public List<Map<String, Object>> getOrdersOfARestaurants(Long restaurantId) throws ResourceNotFoundException {
		List<UserOrder> orders = orderRepository.findUserOrderByRestaurantId(restaurantId);
//		Map<String, Object> restaurantDetails;
		if (orders.size() == 0)
			return Arrays.asList();
//		try {
//			restaurantDetails=restaurantProxyService.getRestaurantById(restaurantId).getBody();
//			restaurantDetails.put("image", null);
//		} catch (ResourceNotFoundException e) {
//			// TODO Auto-generated catch block
//			throw new ResourceNotFoundException("Restaurant not found having id: "+restaurantId);
//		}
		Set<Long> foodItemIds = orders.stream()
				.flatMap(order -> order.getOrderItems().stream().map(orderItem -> orderItem.getFoodItemId()))
				.collect(Collectors.toSet());

		System.out.println(foodItemIds);
		List<Map<String, Object>> foodItemDetails = foodItemProxyService
				.getMultipleFoodItemByIds(new ArrayList<>(foodItemIds)).getBody();

		Set<Long> userIds = orders.stream().map(order -> order.getUserId()).collect(Collectors.toSet());

		Set<Long> userAddressIds = orders.stream().map(order -> order.getUserAddressId()).collect(Collectors.toSet());
		List<Map<String, Object>> userDetails = userProxyService.getMultipleUsersByIds(new ArrayList<Long>(userIds))
				.getBody();
		List<Map<String, Object>> userAddressDetails = userProxyService
				.getMultipleUserAddressIds(new ArrayList<Long>(userAddressIds)).getBody();
		System.out.println(userDetails);
		System.out.println(userAddressDetails);
//		System.out.println(foodItemDetails);
		return orders.stream().map(order -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", order.getId());
			map.put("transactionId", order.getTransactionId());
			map.put("status", order.getOrderStatus());
			map.put("orderedOn", order.getOrderedOn().toString());
			map.put("price", order.getPrice());
//					map.put("restaurantDetails", restaurantDetails);

			map.put("orderItems", foodItemDetails.stream().map(singleFoodItemDetails -> {
				for (OrderItem orderItem : order.getOrderItems()) {
					Map<String, Object> orderItemMap = new HashMap<>();
					if (Long.parseLong(singleFoodItemDetails.get("id").toString()) == orderItem.getFoodItemId()) {
						orderItemMap.put("foodItem", singleFoodItemDetails);
						orderItemMap.put("quantity", orderItem.getQuantity());
						return orderItemMap;
					}
				}
				return null;

			}).filter(orderItem -> orderItem != null).collect(Collectors.toList()));
			map.put("user",
					userDetails.stream()
							.filter(userDetail -> Long.parseLong(userDetail.get("id").toString()) == order.getUserId())
							.findAny().orElse(null));
			map.put("userAddress", userAddressDetails.stream()
					.filter(userAddress -> Long.parseLong(userAddress.get("id").toString()) == order.getUserAddressId())
					.findAny().orElse(null));
			return map;
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> getOrdersOfARestaurantsFallback(Long restaurantId, RuntimeException exception)
			throws ResourceNotFoundException {
		Map<String, Object> defaultResponse = new HashMap<>();
		defaultResponse.put("Message", "Service is currently down. Please try again sometime.");
		List<Map<String, Object>> defaultResponseList = new ArrayList<>();
		defaultResponseList.add(defaultResponse);
		return defaultResponseList;
	}
}
