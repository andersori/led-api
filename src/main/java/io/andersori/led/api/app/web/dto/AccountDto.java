package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.entity.Account;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AccountDto implements Dto<AccountDto, Account> {
	
	@JsonProperty(value = "userid")
	private Long userId;
	private String name;
	private String username;
	private String password;
	@JsonProperty(value = "lastlogin")
	private LocalDateTime lastLogin;
	private Set<RoleLed> roles;

	public AccountDto() {
		this.roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.NONE));
	}
	
	public AccountDto(Account account) {
		this();
		toDto(account);
	}

	@Override
	public void toDto(Account entity) {
		this.setUserId(entity.getAccountId());
		this.setName(entity.getName());
		this.setPassword(entity.getPassword());
		this.setUsername(entity.getUsername());
		this.setRoles(entity.getRoles());
		this.setLastLogin(entity.getLastLogin());
	}

	@Override
	public Account toEntity() {
		Account entity = new Account();
		entity.setAccountId(this.getUserId());
		entity.setName(this.getName());
		entity.setPassword(this.getPassword());
		entity.setUsername(this.getUsername());
		entity.setRoles(this.getRoles());
		entity.setLastLogin(this.getLastLogin());
		return entity;
	}

}
