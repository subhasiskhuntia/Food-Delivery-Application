package com.food.adminservice.exception;

public class ExceptionResponse {
	private String url;
	private String errorMessage;

	public ExceptionResponse() {
		super();
	}

	public ExceptionResponse(String url, String errorMessage) {
		super();
		this.url = url;
		this.errorMessage = errorMessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

