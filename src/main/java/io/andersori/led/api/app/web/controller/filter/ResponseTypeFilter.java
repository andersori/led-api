package io.andersori.led.api.app.web.controller.filter;

import spark.Filter;
import spark.Request;
import spark.Response;

public class ResponseTypeFilter {
	
	public static Filter responseType = (Request req, Response res) -> {
		res.type("application/json;charset=utf-8");
	};
	
	public static Filter addGzipHeader = (Request req, Response res) -> {
		res.header("Content-Encoding", "gzip");
    };
}
