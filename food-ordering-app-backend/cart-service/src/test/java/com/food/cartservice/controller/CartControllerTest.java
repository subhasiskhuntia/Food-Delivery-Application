//package com.food.cartservice.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.food.cartservice.dto.CartDto;
//import com.food.cartservice.dto.CartDto.CartDtoBuilder;
//import com.food.cartservice.service.CartService;
//import com.food.cartservice.service.CartServiceImpl;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//
//class CartControllerTest {
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private CartServiceImpl cartService;
//	
//	CartDto cartDto;
//
//	
//	@BeforeEach
//	public void setUp() {
//	
//	//cartDto=new CartDto();
//	
//	cartDto=CartDto.builder().id(1L).foodItemId(1L).userId(1L).quantity(1L).build();
//	
//	}
//		
//	
//	
//
//	@AfterEach
//	public void tearDown() {
//		cartDto=null;
//	}
//
//	@Test
//	void testAddCartItem()throws Exception {
//		System.out.print(cartDto);
//		when(cartService.addCartItem(any(CartDto.class))).thenReturn(cartDto);
//		String cartJson = this.convertToJson(cartDto);
//
//		RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/cart/addToCart")
//				.contentType(MediaType.APPLICATION_JSON).content(cartJson);
//
//		MvcResult mvcResult = mockMvc.perform(request).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		String returnedUserJson = mvcResult.getResponse().getContentAsString();
//
//		assertEquals(200, status);
//		assertEquals(cartJson, returnedUserJson);
//	}
//
//	@Test
//	void testUpdateCartItem()throws Exception {
//		when(cartService.updateCartItem(any(CartDto.class))).thenReturn(cartDto);
//		String cartJson = this.convertToJson(cartDto);
//
//		RequestBuilder request = MockMvcRequestBuilders.patch("/api/v1/cart/updateCartItem")
//				.contentType(MediaType.APPLICATION_JSON).content(cartJson);
//
//		MvcResult mvcResult = mockMvc.perform(request).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		String returnedUserJson = mvcResult.getResponse().getContentAsString();
//
//		assertEquals(200, status);
//		assertEquals(cartJson, returnedUserJson);
//	}
//
//	@Test
//	void testDeleteCartItme()throws Exception {
//		when(cartService.deleteCartItem(any(CartDto.class))).thenReturn("Cart Item deleted successfully");
//		String cartJson = this.convertToJson(cartDto);
//
//		RequestBuilder request = MockMvcRequestBuilders.delete("/api/v1/cart/deleteCartItem")
//				.contentType(MediaType.APPLICATION_JSON).content(cartJson);
//
//		MvcResult mvcResult = mockMvc.perform(request).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		String returnedUserJson = mvcResult.getResponse().getContentAsString();
//
//		assertEquals(200, status);
//		assertEquals("Cart Item deleted successfully", returnedUserJson);
//		
//	}
//
//	@Test
//	void testGetCartItemsByUserId()throws Exception {
//		List<CartDto> list = new ArrayList<>()	;
//		
//		when(cartService.getCartItemsOfAUser(any(Long.class))).thenReturn(list);
//		String cartJson = this.convertToJson(list);
//
//		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/cart/getCartItemsByUserId/?userId="+cartDto.getUserId())
//				.contentType(MediaType.APPLICATION_JSON).content(cartJson);
//
//		MvcResult mvcResult = mockMvc.perform(request).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		String returnedUserJson = mvcResult.getResponse().getContentAsString();
//
//		assertEquals(200, status);
//		assertEquals(cartJson, returnedUserJson);
//		
//	
//	}
////
////	@Test
////	void testCartController() {
////		fail("Not yet implemented");
////	}
//	private String convertToJson(Object obj) throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		return mapper.writeValueAsString(obj);
//	}
//
//}
//	
package com.food.cartservice.controller;

import com.food.cartservice.dto.CartDto;
import com.food.cartservice.exception.CartForAnotherRestaurantAlreadyExistsException;
import com.food.cartservice.exception.ResourceNotFoundException;
import com.food.cartservice.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    CartService cartService;

    CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cartController = new CartController(cartService);
    }

    @Test
    void addCartItem() throws ResourceNotFoundException, CartForAnotherRestaurantAlreadyExistsException {
        CartDto cartDto = new CartDto();
        cartDto.setUserId(1L);
        cartDto.setFoodItemId(1L);
        when(cartService.addCartItem(any(CartDto.class))).thenReturn(cartDto);

        ResponseEntity<CartDto> responseEntity = cartController.addCartItem(cartDto);

        verify(cartService, times(1)).addCartItem(any(CartDto.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cartDto, responseEntity.getBody());
    }

    @Test
    void updateCartItem() throws ResourceNotFoundException {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setQuantity(2);
        when(cartService.updateCartItem(any(CartDto.class))).thenReturn(cartDto);

        ResponseEntity<CartDto> responseEntity = cartController.updateCartItem(cartDto);

        verify(cartService, times(1)).updateCartItem(any(CartDto.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cartDto, responseEntity.getBody());
    }

    @Test
    void deleteCartItem() throws ResourceNotFoundException {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        when(cartService.deleteCartItem(anyLong())).thenReturn("Cart Item deleted successfully");

        ResponseEntity<String> responseEntity = cartController.deleteCartItme((long)1);

        verify(cartService, times(1)).deleteCartItem(anyLong());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Cart Item deleted successfully", responseEntity.getBody());
    }

    @Test
    void getCartItemsByUserId() throws ResourceNotFoundException {
        Long userId = 1L;
        List<CartDto> cartDtoList = new ArrayList<>();
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        cartDtoList.add(cartDto);
        when(cartService.getCartItemsOfAUser(anyLong())).thenReturn(cartDtoList);

        ResponseEntity<List<CartDto>> responseEntity = cartController.getCartItemsByUserId(userId);

        verify(cartService, times(1)).getCartItemsOfAUser(anyLong());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cartDtoList, responseEntity.getBody());
    }
}

