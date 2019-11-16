package io.andersori.led.api.app.web.util;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864_000_000; // 10days
	public static final String SECRET = "1234";
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
}
