package com.food.foodItemservice.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class ExceptionController {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionResponse handleInvalidArgument(HttpServletRequest request,MethodArgumentNotValidException exception) {
		ExceptionResponse exceptionResponse=new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
		
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public ExceptionResponse handleResourceNotFoundException(HttpServletRequest request,ResourceNotFoundException exception) {
		ExceptionResponse exceptionResponse=new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseBody
	public ExceptionResponse handleSqlIntegrityConstraintViolation(HttpServletRequest request,SQLIntegrityConstraintViolationException exception) {
		ExceptionResponse exceptionResponse=new ExceptionResponse();
		exceptionResponse.setUrl(request.getRequestURI());
		exceptionResponse.setErrorMessage(exception.getMessage());
		return exceptionResponse;
	}
}
