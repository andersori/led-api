package io.andersori.led.api.domain.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
	
	private String message;
	private String field;
	private Object rejectedValue;
	
}
