package io.andersori.led.api.domain.exception;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;

public class ForbiddenExecutionException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.FORBIDDEN;

	public ForbiddenExecutionException(Class<?> classType) {
		super("Please provide a token.", HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}
	
	public ForbiddenExecutionException(String message, Class<?> classType) {
		super(message, HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}

}
