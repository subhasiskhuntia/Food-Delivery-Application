package com.food.cartservice.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.food.cartservice.exception.CartForAnotherRestaurantAlreadyExistsException;
import com.food.cartservice.exception.ResourceNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CartServiceExceptionHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", ex.getMessage());
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(CartForAnotherRestaurantAlreadyExistsException.class)
	public Map<String, String> handleCartForAnotherRestaurantAlreadyExistsException(CartForAnotherRestaurantAlreadyExistsException ex) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", ex.getMessage());
		return errorMap;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public Map<String, String> handleSqlIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
		Map<String, String> errorMap = new HashMap<>();
		System.out.println(ex);
		errorMap.put("errorMessage", ex.getMessage());
		return errorMap;
	}
}
