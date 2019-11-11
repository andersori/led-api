package io.andersori.led.api.app.web.controller.route;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.util.JsonTransformer;
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
				
				try {
					res.status(HttpStatus.CREATED_201);
					return register(account);
				} catch(UnprocessableEntityException e) {
					res.status(400);
					return e.getError();
				}
				
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

	public AccountDto register(AccountDto account) throws UnprocessableEntityException {
		return new AccountDto().toDto(accountService.save(account.toEntity()));
	}
}
