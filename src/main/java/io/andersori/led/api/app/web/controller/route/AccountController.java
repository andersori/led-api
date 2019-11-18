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
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.MissingInformationsException;
import io.andersori.led.api.domain.exception.NotFoundException;
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
	public AccountController(JsonTransformer transformer, @Qualifier("accountService") AccountService accountService) {
		this.transformer = transformer;
		this.accountService = accountService;
	}

	private Object findAll(Request req, Response res) {
		try {
			if (req.queryParams("page") != null && req.queryParams("size") != null) {

				int page = Integer.parseInt(req.queryParams("page"));
				int size = Integer.parseInt(req.queryParams("size"));

				return accountService.findAll(page, size).stream().map(user -> {
					return new AccountDto(user);
				}).collect(Collectors.toList());

			} else if (req.queryParams("page") != null) {
				throw new MissingInformationsException("Please provide page size.(size)", AccountController.class);
			} else if (req.queryParams("size") != null) {
				throw new MissingInformationsException("Provide page number.(page)", AccountController.class);
			}

			return accountService.findAll().stream().map(user -> {
				return new AccountDto(user);
			}).collect(Collectors.toList());
		} catch (MethodNotAllowedException | MissingInformationsException e) {
			ApiError error = new ApiError(e.getMessage());
			error.setClassType(e.getClassType());

			res.status(e.getHttpStatusCode());
			return error;
		} catch (Exception e) {
			ApiError error = new ApiError(e.getMessage());
			error.setClassType(AccountController.class.getSimpleName());
			res.status(HttpStatus.BAD_REQUEST_400);
			return error;
		}
	}

	private Object save(Request req, Response res) {
		if (!req.body().isEmpty()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				AccountDto account = mapper.readValue(req.body(), AccountDto.class);

				if (account.getRoles().size() == 1 && !account.getRoles().contains(RoleLed.DEFAULT)) {
					account.getRoles().add(RoleLed.DEFAULT);
				}

				if (account.getPassword() != null) {
					account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
				}

				res.status(HttpStatus.CREATED_201);
				return new AccountDto(accountService.save(account.toEntity()));
			} catch (JsonProcessingException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountDto.class.getSimpleName());
				return error;
			} catch (ForbiddenExecutionException | ConflictException | MethodNotAllowedException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());

				res.status(e.getHttpStatusCode());
				return error;
			} catch (UnprocessableEntityException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());
				error.setSubErrors(e.getErrors().stream().map(subError -> {
					return new ApiSubError(subError.getField(), subError.getRejectedValue(), subError.getMessage());
				}).collect(Collectors.toList()));

				res.status(e.getHttpStatusCode());
				return error;
			} catch (Exception e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountController.class.getSimpleName());
				res.status(HttpStatus.BAD_REQUEST_400);
				return error;
			}
		}
		return null;
	}

	private Object get(Request req, Response res) {
		if (!req.params(":id").isEmpty()) {
			try {
				Long id = Long.parseLong(req.params(":id"));
				return accountService.findById(id);
			} catch (MethodNotAllowedException | NotFoundException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());

				res.status(e.getHttpStatusCode());
				return error;
			} catch (Exception e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountController.class.getSimpleName());
				res.status(HttpStatus.BAD_REQUEST_400);
				return error;
			}
		}
		return null;
	}

	private Object delete(Request req, Response res) {
		if (!req.params(":id").isEmpty()) {
			try {
				Long id = Long.parseLong(req.params(":id"));
				accountService.delete(id);
				res.status(HttpStatus.NO_CONTENT_204);
			} catch (MethodNotAllowedException | NotFoundException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());

				res.status(e.getHttpStatusCode());
				return error;
			} catch (Exception e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountController.class.getSimpleName());
				res.status(HttpStatus.BAD_REQUEST_400);
				return error;
			}
		}
		return null;
	}

	public Object update(Request req, Response res) {
		if (!req.params(":id").isEmpty()) {
			try {
				Long id = Long.parseLong(req.params(":id"));

				if (!req.body().isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					AccountDto accountReceived = mapper.readValue(req.body(), AccountDto.class);

					if (accountReceived.getRoles().size() == 1
							&& !accountReceived.getRoles().contains(RoleLed.DEFAULT)) {
						accountReceived.getRoles().add(RoleLed.DEFAULT);
					}

					if (accountReceived.getPassword() != null) {
						accountReceived.setPassword(BCrypt.hashpw(accountReceived.getPassword(), BCrypt.gensalt()));
					}

					return new AccountDto(accountService.update(id, accountReceived.toEntity()));

				} else {
					throw new MissingInformationsException("Please provide some information.", AccountDto.class);
				}

			} catch (JsonProcessingException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountDto.class.getSimpleName());
				return error;
			} catch (MissingInformationsException | ConflictException | MethodNotAllowedException
					| NotFoundException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());

				res.status(e.getHttpStatusCode());
				return error;
			} catch (UnprocessableEntityException e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(e.getClassType());
				error.setSubErrors(e.getErrors().stream().map(subError -> {
					return new ApiSubError(subError.getField(), subError.getRejectedValue(), subError.getMessage());
				}).collect(Collectors.toList()));

				res.status(e.getHttpStatusCode());
				return error;
			} catch (Exception e) {
				ApiError error = new ApiError(e.getMessage());
				error.setClassType(AccountController.class.getSimpleName());
				res.status(HttpStatus.BAD_REQUEST_400);
				return error;
			}
		}
		return null;
	}

	@Override
	public void addRoutes() {
		Spark.get("", (req, res) -> findAll(req, res), transformer);
		Spark.get("/:id", (req, res) -> get(req, res), transformer);
		Spark.post("", (req, res) -> save(req, res), transformer);
		Spark.delete("/:id", (req, res) -> delete(req, res), transformer);
		Spark.put("/:id", (req, res) -> update(req, res), transformer);
	}

}
