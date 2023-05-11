package com.food.orderservice.controller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.orderservice.dto.OrderDto;
import com.food.orderservice.dto.OrderItemDto;
import com.food.orderservice.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	OrderDto orderDto;
	OrderItemDto orderItemDto;

	@BeforeEach
	public void setUp() {
		orderItemDto = new OrderItemDto();

		orderItemDto = OrderItemDto.builder().id(1l).quantity(1).foodItemId(1L).build();
		List<OrderItemDto> orderitems = new ArrayList<>();
		orderitems.add(orderItemDto);
		orderDto = new OrderDto();
		orderDto = OrderDto.builder().id(1l).orderItems(orderitems).orderStatus("delivered").price(2700)
				.userAddressId(1L).userId(1L).transactionId("12345").build();

	}

	@AfterEach
	public void tearDown() {
		orderItemDto = null;
		orderDto = null;
	}

	@Test
	@DisplayName("test placeOrder method of OrderController class")
	void testPlaceOrder() throws Exception {
		when(orderService.placeOrder(any(OrderDto.class))).thenReturn(orderDto);

		String orderDtoJson = this.convertToJson(orderDto);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/order/placeOrder")
				.contentType(MediaType.APPLICATION_JSON).content(orderDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedCartDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(201, status);
		assertEquals(orderDtoJson, returnedCartDtoJson);
	}

	@Test
	@DisplayName("test cancelOrder method of OrderController class")
	void testCancelOrder() throws Exception {
		when(orderService.deleteOrder(any(Long.class))).thenReturn("Order cancelled successfully");

		String orderDtoJson = this.convertToJson(orderDto);

		RequestBuilder request = MockMvcRequestBuilders.delete("/api/v1/order/cancelOrder/?id=" + orderDto.getUserId())
				.contentType(MediaType.APPLICATION_JSON).content(orderDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedCartDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals("Order cancelled successfully", returnedCartDtoJson);
	}

	@Test
	void testUpdateOrderStatus() throws Exception {
		when(orderService.updateOrderStatus(any(String.class), any(Long.class))).thenReturn(orderDto);

		String orderDtoJson = this.convertToJson(orderDto);

		RequestBuilder request = MockMvcRequestBuilders.patch("/api/v1/order/updateOrderStatus").param("id", "1")
				.param("status", orderDto.getOrderStatus()).contentType(MediaType.APPLICATION_JSON)
				.content(orderDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedCartDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(orderDtoJson, returnedCartDtoJson);
	}

	@Test
	void testGetUserOrder() throws Exception {
		List<OrderDto> list = new ArrayList<>();
		list.add(orderDto);
		when(orderService.getUserOrders(any(Long.class))).thenReturn(list);

		String orderDtoJson = this.convertToJson(list);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/order/getUsersOrder").param("userId", "1")
				.contentType(MediaType.APPLICATION_JSON).content(orderDtoJson);

		MvcResult mvcResult = mockMvc.perform(request).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String returnedCartDtoJson = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		assertEquals(orderDtoJson, returnedCartDtoJson);
	}

	private String convertToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

}
