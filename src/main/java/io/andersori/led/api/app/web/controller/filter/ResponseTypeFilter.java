package io.andersori.led.api.app.web.controller.filter;

import org.springframework.stereotype.Component;

import spark.Spark;

@Component
public class ResponseTypeFilter {

	public ResponseTypeFilter() {
		Spark.after((req, res) -> {
			res.type("application/json;charset=utf-8");
		});
	}
}
