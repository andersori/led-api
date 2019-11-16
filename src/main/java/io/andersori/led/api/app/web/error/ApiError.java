package io.andersori.led.api.app.web.error;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonInclude(Include.NON_NULL)
public class ApiError {

	@JsonProperty(value = "time_stamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@Setter(value = AccessLevel.PRIVATE)
	private LocalDateTime timeStamp;
	
	@JsonProperty(value = "class_type")
	private String classType;
	
	@JsonProperty(value = "message")
	private String message;
	
	@JsonProperty(value = "sub_errors")
	private List<ApiSubError> subErrors;

	private ApiError() {
		timeStamp = LocalDateTime.now();
	}

	public ApiError(String message) {
		this();
		this.message = message;
	}
	public ApiError(String message, List<ApiSubError> subErrors) {
		this(message);
		this.subErrors = subErrors;
	}

}
