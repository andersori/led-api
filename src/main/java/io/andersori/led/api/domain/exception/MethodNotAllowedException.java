package io.andersori.led.api.domain.exception;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;

public class MethodNotAllowedException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.METHOD_NOT_ALLOWED;

	public MethodNotAllowedException(String message, Class<?> classType) {
		super(message, HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}

}
