package io.andersori.led.api.domain.exception;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import io.andersori.led.api.domain.error.ErrorInfo;

public class UnprocessableEntityException extends DomainException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus.Code HTTP_STATUS = HttpStatus.Code.UNPROCESSABLE_ENTITY;

	public UnprocessableEntityException(Class<?> classType, List<ErrorInfo> errors) {
		super(classType.getSimpleName() + " validation error.", HTTP_STATUS.getCode(), classType.getSimpleName(),
				errors);
	}

}
