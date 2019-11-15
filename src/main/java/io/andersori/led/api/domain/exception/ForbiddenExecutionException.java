package io.andersori.led.api.domain.exception;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import io.andersori.led.api.domain.error.ErrorInfo;

public class ForbiddenExecutionException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.FORBIDDEN;
	
	public ForbiddenExecutionException(String message, Class<?> classType, List<ErrorInfo> errors) {
		super(message, HTTP_STATUS.getCode(), classType != null ? classType.getSimpleName() : "", errors);
	}

}
