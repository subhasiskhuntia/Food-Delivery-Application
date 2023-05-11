package com.food.restaurantservice.exceptions;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

@ControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionResponse handleInvalidArgument(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(UserNotFoundException.class)
	public ExceptionResponse handleBusinessException(HttpServletRequest request, UserNotFoundException exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	@ExceptionHandler(RestaurantAlreadyExistsException.class)
	public ExceptionResponse handleRestaurantAlreadyExistsException(HttpServletRequest request,
			RestaurantAlreadyExistsException exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}


	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(RestaurantNotFoundException.class)
	public ExceptionResponse handleRestaurantNotFoundException(HttpServletRequest request,
			RestaurantNotFoundException exception) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}

}
