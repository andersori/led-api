package io.andersori.led.api.app.web.util;

public class SecurityConstants {

	public static final long EXPIRATION_TIME;
	public static final String SECRET;
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

	static {
		EXPIRATION_TIME = System.getenv("EXPIRATION_TIME") != null ? Long.parseLong(System.getenv("EXPIRATION_TIME"))
				: 864_000_000; // 10days
		SECRET = System.getenv("SECRET") != null ? System.getenv("SECRET") : "1234";
	}
}
