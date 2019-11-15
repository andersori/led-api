package io.andersori.led.api.app.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import io.andersori.led.api.app.web.dto.AccountDto;
import io.andersori.led.api.app.web.util.AccountContext;

public class AuditorAwareImpl implements AuditorAware<AccountDto> {

	@Override
	public Optional<AccountDto> getCurrentAuditor() {
		return Optional.of(AccountContext.getAccount());
	}

}
