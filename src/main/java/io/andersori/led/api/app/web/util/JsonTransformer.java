package io.andersori.led.api.app.web.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import spark.ResponseTransformer;

@Component
public class JsonTransformer implements ResponseTransformer {

	@Override
	public String render(Object model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(model);
	}

}
