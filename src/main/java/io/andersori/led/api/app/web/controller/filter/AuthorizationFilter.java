package io.andersori.led.api.app.web.controller.filter;

import org.springframework.stereotype.Component;

import spark.Spark;

@Component
public class AuthorizationFilter {
	
	public AuthorizationFilter() {
		Spark.before((req, res) -> {
			
		});
	}
}
