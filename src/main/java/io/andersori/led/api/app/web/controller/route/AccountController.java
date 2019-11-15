package io.andersori.led.api.app.web.controller.route;

import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.error.ApiError;
import io.andersori.led.api.app.web.error.ApiSubError;
import io.andersori.led.api.app.web.util.JsonTransformer;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.service.AccountService;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;

@Controller
public class AccountController implements RouteGroup {

	private final JsonTransformer transformer;
	private final AccountService accountService;

	@Autowired
	public AccountController(JsonTransformer transformer, @Qualifier("acountService") AccountService accountService) {
		this.transformer = transformer;
		this.accountService = accountService;
	}

	private Object findAll(Request req, Response res) {
		try {
			return accountService.findAll().stream().map(user -> {
				return new AccountDto().toDto(user);
			}).collect(Collectors.toList());
		} catch (ForbiddenExecutionException e) {
			e.setClassType(AccountDto.class.getSimpleName());
			
			ApiError error = new ApiError(e.getMessage());
			error.setClassType(e.getClassType());
			error.setSubErrors(e.getErrors().stream().map(subError -> {
				return new ApiSubError(subError.getField(), subError.getRejectedValue(), subError.getMessage());
			}).collect(Collectors.toList()));

			res.status(e.getHttpStatusCode());
			return error;
		}
	}

	private Object save(Request req, Response res) {
		if (!req.body().isEmpty()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				AccountDto account = mapper.readValue(req.body(), AccountDto.class);

				if (account.getPassword() != null) {
					account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
				}

				res.status(HttpStatus.CREATED_201);
				return new AccountDto().toDto(accountService.save(account.toEntity()));
			} catch (JsonProcessingException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountDto.class.getSimpleName());
				return error;
			} catch (UnprocessableEntityException | ConflictException | ForbiddenExecutionException e) {
				if(e instanceof ForbiddenExecutionException) {
					e.setClassType(AccountDto.class.getSimpleName());
				}
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());
				error.setSubErrors(e.getErrors().stream().map(subError -> {
					return new ApiSubError(subError.getField(), subError.getRejectedValue(), subError.getMessage());
				}).collect(Collectors.toList()));

				res.status(e.getHttpStatusCode());
				return error;
			} catch (Exception e) {
				ApiError error = new ApiError("Unexoected Error");
				error.setClassType(AccountDto.class.getSimpleName());
				res.status(HttpStatus.BAD_REQUEST_400);
				return error;
			}
		}
		return null;
	}

	@Override
	public void addRoutes() {
		Spark.get("", (req, res) -> {
			return findAll(req, res);
		}, transformer);
		Spark.post("", (req, res) -> save(req, res), transformer);
	}

}
