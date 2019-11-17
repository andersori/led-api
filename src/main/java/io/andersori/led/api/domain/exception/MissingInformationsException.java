package io.andersori.led.api.domain.exception;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;

public class MissingInformationsException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.BAD_REQUEST;
	
	public MissingInformationsException(String message, Class<?> classType) {
		super(message, HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}

}
