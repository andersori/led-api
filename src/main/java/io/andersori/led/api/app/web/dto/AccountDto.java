package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.entity.Account;
import lombok.Data;

@Data
public class AccountDto implements DtoMapper<Account, AccountDto> {
	
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

	@Override
	public AccountDto toDto(Account entity) {
		AccountDto dto = new AccountDto();
		dto.setUserId(entity.getAccountId());
		dto.setName(entity.getName());
		dto.setPassword(entity.getPassword());
		dto.setUsername(entity.getUsername());
		dto.setRoles(entity.getRoles());
		dto.setLastLogin(entity.getLastLogin());
		return dto;
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
