package io.andersori.led.api.domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import io.andersori.led.api.domain.error.ErrorInfo;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;

public class ValidatorEntity {
	public static <T> void validate(T entity, Class<?> classType) throws UnprocessableEntityException {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<T>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			List<ErrorInfo> errors = new ArrayList<>();

			for (ConstraintViolation<T> violation : violations) {
				ErrorInfo error = new ErrorInfo();
				error.setField(violation.getPropertyPath().toString());
				error.setMessage(violation.getMessage());
				error.setRejectedValue(violation.getInvalidValue());

				errors.add(error);
			}

			throw new UnprocessableEntityException(classType, errors);
		}
	}
}
