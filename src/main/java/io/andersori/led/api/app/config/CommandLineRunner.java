package io.andersori.led.api.app.config;

import java.util.Arrays;
import java.util.HashSet;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.service.AccountService;

@Component
public class CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineRunner.class);

	@Autowired
	public CommandLineRunner(@Qualifier("accountServiceImp") AccountService accountService)
			throws UnprocessableEntityException, ConflictException, MethodNotAllowedException,
			ForbiddenExecutionException {
		try {
			accountService.findByUsername("admin");
			LOGGER.info("Admin already registered.");
		} catch (NotFoundException e) {
			Account ac = new Account();
			ac.setName("Administrator");
			ac.setUsername("admin");
			ac.setPassword(BCrypt.hashpw("1234", BCrypt.gensalt()));
			ac.setRoles(new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT, RoleLed.ADMIN)));
			accountService.save(ac);
			LOGGER.info("Admin saved.");
		}
	}
}
