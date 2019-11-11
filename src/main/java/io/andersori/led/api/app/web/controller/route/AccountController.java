package io.andersori.led.api.app.web.controller.route;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.response.ResponseData;
import io.andersori.led.api.app.web.util.JsonTransformer;
import io.andersori.led.api.domain.error.ConstraintViolationDescription;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.service.AccountService;
import spark.Spark;

@Controller
public class AccountController {

	private final AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService, JsonTransformer transformer) {
		this.accountService = accountService;

		Spark.get("/accounts", (req, res) -> {
			return findAll();
		}, transformer);

		Spark.post("/accounts", (req, res) -> {
			
			if(!req.body().isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				AccountDto account = mapper.readValue(req.body(), AccountDto.class);
				
				if(account.getPassword()!=null) {
					account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
				}

				ResponseData<?> responseData = register(account);
				responseData.setPath("/accounts");
				res.status(responseData.getStatusCode());
				return responseData;
			}
			res.status(400);
			return "{\"msg\":\"Erro\"}";
		}, transformer);
	}

	public List<AccountDto> findAll() {
		return accountService.findAll().stream().map(user -> {
			return new AccountDto().toDto(user);
		}).collect(Collectors.toList());
	}

	public ResponseData<?> register(AccountDto account) {
		try {
			ResponseData<AccountDto> response = new ResponseData<AccountDto>();
			response.setTimeStamp(LocalDateTime.now());
			response.setMessage("Account registered successfully");
			response.setStatusCode(201);
			response.setBody(Arrays.asList(new AccountDto().toDto(accountService.save(account.toEntity()))));
			return response;
		} catch(UnprocessableEntityException e) {
			ResponseData<ConstraintViolationDescription> response = new ResponseData<ConstraintViolationDescription>();
			response.setTimeStamp(LocalDateTime.now());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setBody(e.getError().getErrors());
			return response;
		}
	}
}
