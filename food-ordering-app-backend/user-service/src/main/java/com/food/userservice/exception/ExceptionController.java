package com.food.userservice.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException exception,
			HttpServletRequest request) {
		ExceptionResponse response = new ExceptionResponse();
		response.setUrl(request.getRequestURI());
		response.setErrorMessage(exception.getMessage());
		return response;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ExceptionResponse handleException(Exception exception, HttpServletRequest request) {
		ExceptionResponse response = new ExceptionResponse();
		response.setUrl(request.getRequestURI());
		response.setErrorMessage(exception.getMessage());
		return response;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ExceptionResponse handleInvalidUsernamOrPasswordException(InvalidUsernamOrPasswordException exception,
			HttpServletRequest request) {
		ExceptionResponse response = new ExceptionResponse();
		response.setUrl(request.getRequestURI());
		response.setErrorMessage(exception.getMessage());
		return response;
	}
}
