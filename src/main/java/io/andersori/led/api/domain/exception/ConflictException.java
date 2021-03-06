package io.andersori.led.api.domain.exception;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;

public class ConflictException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.CONFLICT;

	public ConflictException(String message, Class<?> classType) {
		super(message, HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}

}
