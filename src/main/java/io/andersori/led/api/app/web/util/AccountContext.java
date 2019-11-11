package io.andersori.led.api.app.web.util;

import io.andersori.led.api.app.web.dto.AccountDto;

public class AccountContext {
	
	private static final ThreadLocal<String> accountContext = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return "led_default_auditor";
		}
	};

	public static void setAccount(AccountDto account) {
		accountContext.set(account.getUsername());
	}

	public static String getAccount() {
		return accountContext.get();
	}
}
