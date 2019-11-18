package io.andersori.led.api.app.web.controller.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.error.ApiError;
import io.andersori.led.api.app.web.util.AccountContext;
import io.andersori.led.api.app.web.util.JsonTransformer;
import io.andersori.led.api.app.web.util.SecurityConstants;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.service.AccountService;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class AuthorizationFilter implements Filter {

	private final JsonTransformer transformer;
	private final AccountService accountService;

	@Autowired
	public AuthorizationFilter(JsonTransformer transformer,
			@Qualifier("accountServiceImp") AccountService accountService) {
		this.transformer = transformer;
		this.accountService = accountService;
	}

	@Override
	public void handle(Request request, Response response) throws Exception {
		boolean authorized = false;
		String msg = "";

		try {
			String token = request.headers(SecurityConstants.HEADER_STRING);
			if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {

				String username = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
						.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();

				if (username != null) {
					Account ac = accountService.findByUsername(username);
					AccountContext.setAccount(new AccountDto(ac));
					authorized = true;
				}

			} else if (request.pathInfo().endsWith("tokens")
					|| (request.pathInfo().endsWith("accounts") && request.requestMethod().equals("POST"))) {
				AccountDto account = new AccountDto();
				account.setName("AUDITOR");
				account.setUsername("led_default_auditor");
				AccountContext.setAccount(account);
				authorized = true;
			} else {
				throw new ForbiddenExecutionException(AuthorizationFilter.class);
			}

		} catch (SignatureVerificationException | JWTDecodeException | TokenExpiredException e) {
			ApiError error = new ApiError(e.getMessage());
			error.setClassType(JWT.class.getSimpleName());

			msg = transformer.render(error);
		} catch (NotFoundException | ForbiddenExecutionException e) {
			ApiError error = new ApiError(
					e instanceof NotFoundException ? "Account provided in token does not exist" : e.getMessage());
			error.setClassType(e.getClassType());

			msg = transformer.render(error);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!authorized) {
			Spark.halt(401, msg);
		}

	}
}
