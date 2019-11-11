package io.andersori.led.api.domain.exception;

import io.andersori.led.api.domain.error.ApiError;

public class UnprocessableEntityException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private ApiError error;
	
	public UnprocessableEntityException(ApiError error) {
		super(error.getMessage());
		this.error = error;
	}
	
	public ApiError getError() {
		return error;
	}
}
