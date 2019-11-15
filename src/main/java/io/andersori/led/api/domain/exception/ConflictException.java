package io.andersori.led.api.domain.exception;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import io.andersori.led.api.domain.error.ErrorInfo;

public class ConflictException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.CONFLICT;

	public ConflictException(String message, Class<?> classType, List<ErrorInfo> errors) {
		super(message, HTTP_STATUS.getCode(), classType.getSimpleName(), errors);
	}

}
