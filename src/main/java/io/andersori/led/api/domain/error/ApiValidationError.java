package io.andersori.led.api.domain.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError<RejectedValueType> extends ApiSubError {
	private String object;
	private String field;
	private RejectedValueType rejectedValue;
	private String message;
}
