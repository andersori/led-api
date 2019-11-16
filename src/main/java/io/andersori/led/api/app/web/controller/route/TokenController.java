package io.andersori.led.api.app.web.controller.route;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.error.ApiError;
import io.andersori.led.api.app.web.error.ApiSubError;
import io.andersori.led.api.app.web.util.JsonTransformer;
import io.andersori.led.api.app.web.util.SecurityConstants;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.service.AccountService;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;

@Controller
public class TokenController implements RouteGroup {

	private final JsonTransformer transformer;
	private final AccountService accountService;

	@Autowired
	public TokenController(JsonTransformer transformer, AccountService accountService) {
		this.transformer = transformer;
		this.accountService = accountService;
	}

	private Object generate(Request req, Response res) {
		try {
			if (!req.body().isEmpty()) {
				AccountDto account = new ObjectMapper().readValue(req.body(), AccountDto.class);
				Optional<String> token = getToken(account);

				Map<String, String> tokenResponse = new HashMap<String, String>();
				
				if (token.isPresent()) {
					tokenResponse.put("token", token.get());
				} else {
					tokenResponse.put("token", " - ");
				}

				return tokenResponse;
			}
		} catch (NotFoundException e) {
			ApiError error = new ApiError(e.getMessage());
			error.setClassType(e.getClassType());
			error.setSubErrors(e.getErrors().stream().map(subError -> {
				return new ApiSubError(subError.getField(), subError.getRejectedValue(), subError.getMessage());
			}).collect(Collectors.toList()));

			res.status(e.getHttpStatusCode());
			return error;
		} catch (Exception e) {
			ApiError error = new ApiError("Unexoected Error");
			error.setClassType(AccountController.class.getSimpleName());
			res.status(HttpStatus.BAD_REQUEST_400);
			return error;
		}

		return null;
	}

	private Optional<String> getToken(AccountDto account) throws NotFoundException {
		Optional<AccountDto> result = Optional.of(new AccountDto(accountService.findByUsername(account.getUsername())));

		if (result.isPresent()) {
			if (BCrypt.checkpw(account.getPassword(), result.get().getPassword())) {
				return Optional.of(SecurityConstants.TOKEN_PREFIX + JWT.create().withSubject(account.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.sign(Algorithm.HMAC512((SecurityConstants.SECRET.getBytes()))));
			}
		}

		return Optional.empty();
	}

	@Override
	public void addRoutes() {
		Spark.post("", (req, res) -> generate(req, res), transformer);
	}
}
