package io.andersori.led.api.domain.error;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

@Data
public class ApiError {

	private HttpStatus.Code status;
	@JsonProperty(value = "timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime timeStamp;
	private String message;
	private List<ApiSubError> subErrors;

	private ApiError() {
		timeStamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus.Code status, String message, List<ApiSubError> subErrors) {
		this();
		this.status = status;
		this.message = message;
		this.subErrors = subErrors;
	}
}
