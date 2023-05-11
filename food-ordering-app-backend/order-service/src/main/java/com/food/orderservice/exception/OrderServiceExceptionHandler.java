package com.food.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class OrderServiceExceptionHandler {
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
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionResponse handleResourceNotFoundException(HttpServletRequest request,ResourceNotFoundException exception){
    	ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ExceptionResponse handleSqlIntegrityConstraintViolation(HttpServletRequest request,SQLIntegrityConstraintViolationException exception){
    	ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
    }
}
