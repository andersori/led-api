package io.andersori.led.api.domain.exception;

import java.util.Arrays;

import org.eclipse.jetty.http.HttpStatus;

public class NotFoundException extends DomainException {
	
	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.NOT_FOUND;
	
	public NotFoundException(Class<?> classType) {
		super(classType.getSimpleName() + " not found.", HTTP_STATUS.getCode(), classType.getSimpleName(), Arrays.asList());
	}

}
