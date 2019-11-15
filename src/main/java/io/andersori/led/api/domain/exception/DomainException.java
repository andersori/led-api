package io.andersori.led.api.domain.exception;

import java.util.List;

import io.andersori.led.api.domain.error.ErrorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DomainException extends Exception {

	private static final long serialVersionUID = 1L;

	private int httpStatusCode;
	private String classType;
	private List<ErrorInfo> errors;

	public DomainException(String message, int httpStatusCode, String classType, List<ErrorInfo> errors) {
		super(message);
		this.httpStatusCode = httpStatusCode;
		this.classType = classType;
		this.errors = errors;
	}

}
