package com.food.orderservice.service;


import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.dto.OrderItemDto;
import com.food.orderservice.exception.ResourceNotFoundException;
import com.food.orderservice.feign.FoodItemProxyService;
import com.food.orderservice.feign.UserProxyService;
import com.food.orderservice.model.OrderItem;
import com.food.orderservice.model.UserOrder;
import com.food.orderservice.repository.OrderItemRepository;
import com.food.orderservice.repository.OrderRepository;

class OrderServiceImplTest {
	
	@Mock 
	private OrderRepository orderRepository;
	
	@Mock 
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private FoodItemProxyService foodItemProxyService;
	
	@Mock
	private UserProxyService userProxyService;
	
	private OrderService orderService;
	
	OrderDto orderDto;
	OrderItemDto orderItemDto;
	UserOrder userOrder;
	OrderItem orderItem;
	List<OrderItem> orderItems;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		orderService=new OrderServiceImpl(orderRepository,orderItemRepository,userProxyService,foodItemProxyService);
		orderItemDto = new OrderItemDto();

		orderItemDto = OrderItemDto.builder().id(1l).quantity(1).foodItemId(1L).build();
		List<OrderItemDto> orderitemsDto = new ArrayList<>();
		orderitemsDto.add(orderItemDto);
		orderDto = new OrderDto();
		orderDto = OrderDto.builder().id(1l).orderItems(orderitemsDto).orderStatus("ordered").price(2700)
				.userAddressId(1L).userId(1L).transactionId("12345").build();
		orderItem=new OrderItem();
		orderItem=OrderItem.builder().id(1l).quantity(1).foodItemId(1l).build();
		
		userOrder=new UserOrder();
		userOrder=UserOrder.builder().id(1l).orderStatus("ordered").price(2700).transactionId("12345").userAddressId(1l).userId(1l).build();
		orderItems=new ArrayList<>();
		orderItems.add(orderItem);
		userOrder.setOrderItems(orderItems);
		orderItem.setOrder(userOrder);
		
	}

	@AfterEach
	public void tearDown() {
		orderItemDto = null;
		orderDto = null;
		userOrder=null;
		orderItem=null;
	}


	@Test
	@DisplayName("test register method of OrderController class")
	void testPlaceOrder() throws Exception {
		when(userProxyService.addressExistByIdOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodItemProxyService.multipleFoodItemExistOrNot(any(List.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(orderRepository.save(any(UserOrder.class))).thenReturn(userOrder);
		when(orderItemRepository.saveAll(any(List.class))).thenReturn(orderItems);
		
		OrderDto result=orderService.placeOrder(orderDto);
		assertEquals(orderDto, result);
	}
	@Test
	@DisplayName("test register when user not exist method of OrderController class")
	void testPlaceOrderUserNotExist() throws Exception {
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));		
		assertThrows(ResourceNotFoundException.class, ()->orderService.placeOrder(orderDto));
	}
	@Test
	@DisplayName("test register method user odress not exist of OrderController class")
	void testPlaceOrderuserAddressNotExist() throws Exception {
		when(userProxyService.addressExistByIdOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));

		assertThrows(ResourceNotFoundException.class, ()->orderService.placeOrder(orderDto));
	}
	@Test
	@DisplayName("test register when MultipleItemsNotexists method of OrderController class")
	void testPlaceOrderMultipleItemsNotExist() throws Exception {
		when(userProxyService.addressExistByIdOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(userProxyService.userExistOrNot(any(Long.class))).thenReturn(new ResponseEntity<Boolean>(true,HttpStatus.OK));
		when(foodItemProxyService.multipleFoodItemExistOrNot(any(List.class))).thenReturn(new ResponseEntity<Boolean>(false,HttpStatus.OK));
	
		assertThrows(ResourceNotFoundException.class, ()->orderService.placeOrder(orderDto));
	}

	@Test
	@DisplayName("test deleteOrder method of OrderController class")
	void testDeleteOrder() {
		doNothing().when(orderRepository).deleteById(any(Long.class));
		String result=orderService.deleteOrder(orderDto.getUserId());
		
		assertEquals("Order cancelled successfully", result);
		verify(orderRepository,times(1)).deleteById(any(Long.class));
	}

	@Test
	@DisplayName("test updateOrderStatus method of OrderController class")
	void testUpdateOrderStatus() throws Exception{
		Optional<UserOrder> optionalUserOrder=Optional.ofNullable(userOrder);
		when(orderRepository.save(any(UserOrder.class))).thenReturn(userOrder);
		when(orderRepository.findById(any(Long.class))).thenReturn(optionalUserOrder);
		OrderDto result=orderService.updateOrderStatus(orderDto.getOrderStatus(),orderDto.getId());
		assertEquals(orderDto, result);
	}
	@Test
	@DisplayName("Negative test updateOrderTest method of OrderController class")
	void testUpdateOrderStatusResourceNotFoundException() throws Exception{
		Optional<UserOrder> optionalUserOrder=Optional.ofNullable(null);
		
		when(orderRepository.save(any(UserOrder.class))).thenReturn(userOrder);


		assertThrows(ResourceNotFoundException.class, ()->orderService.updateOrderStatus(orderDto.getOrderStatus(),orderDto.getId()));

	}

	@Test
	@DisplayName("test getUserOrders method of OrderController class")
	void testGetUserOrders() {
		List<UserOrder> userOrders=new ArrayList<>();
		userOrders.add(userOrder);
		when(orderRepository.findUserOrdersByUserId(any(Long.class))).thenReturn(userOrders);
		
		List<Map<String, Object>> result=orderService.getUserOrders(orderDto.getUserId());
		
		assertNotNull(result);
		
	}

}
