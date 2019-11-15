package io.andersori.led.api.app.web.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiSubError {
	private String field;
	@JsonProperty(value = "regected_value")
	private Object rejectedValue;
	private String message;
}
