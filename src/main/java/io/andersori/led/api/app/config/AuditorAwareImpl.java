package io.andersori.led.api.app.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import io.andersori.led.api.app.web.util.AccountContext;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(AccountContext.getAccount());
	}

}
