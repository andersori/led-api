package io.andersori.led.api.app.web.controller.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.util.AccountContext;
import io.andersori.led.api.app.web.util.SecurityConstants;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.service.AccountService;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class AuthorizationFilter implements Filter {

	private final AccountService accountService;

	@Autowired
	public AuthorizationFilter(@Qualifier("accountService") AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public void handle(Request request, Response response) throws Exception {
		if (!request.pathInfo().endsWith("token")) {
			boolean authorized = false;
			String msg = "{\"msg\":\"Get away!\"}";

			String token = request.headers(SecurityConstants.HEADER_STRING);

			if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				try {
					String username = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
							.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();

					if (username != null) {
						try {
							Account ac = accountService.findByUsername(username);
							AccountContext.setAccount(new AccountDto(ac));
							authorized = true;
						} catch (NotFoundException e) {
							msg = "{\"msg\":\"Invalid Username!\"}";
						}

					}

				} catch (SignatureVerificationException e) {
					msg = "{\"msg\":\"Invalid Token!\"}";
				} catch (JWTDecodeException e) {
					msg = "{\"msg\":\"Invalid JSON!\"}";
				}

			}

			if (!authorized) {
				Spark.halt(401, msg);
			}
		}
	}
}
