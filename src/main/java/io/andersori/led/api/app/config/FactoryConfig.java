package io.andersori.led.api.app.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.andersori.led.api.app.config.factory.ServiceFactory;
import io.andersori.led.api.domain.service.AccountService;
import io.andersori.led.api.domain.service.AccountServiceImp;

@Configuration
public class FactoryConfig {

	@Bean
	public AccountService acountService(ApplicationContext ctx) throws Exception {
		return new ServiceFactory<AccountService>(AccountService.class, AccountServiceImp.class, ctx).createInstance();
	}
}
