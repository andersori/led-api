package io.andersori.led.api.app.web.util;

import io.andersori.led.api.app.web.dto.AccountDto;

public class AccountContext {
	
	private static final ThreadLocal<AccountDto> accountContext = new ThreadLocal<AccountDto>() {
		@Override
		protected AccountDto initialValue() {
			AccountDto account = new AccountDto();
			account.setName("AUDITOR");
			account.setUsername("led_default_auditor");
			return account;
		}
	};

	public static void setAccount(AccountDto account) {
		accountContext.set(account);
	}

	public static AccountDto getAccount() {
		return accountContext.get();
	}
}
