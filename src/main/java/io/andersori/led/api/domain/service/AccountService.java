package io.andersori.led.api.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.policy.Restrictions;

@Service
public interface AccountService {

	@Restrictions({ RoleLed.ADMIN })
	Account save(Account account) throws UnprocessableEntityException, ConflictException, MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	void delete(Long id) throws MethodNotAllowedException, NotFoundException;

	@Restrictions({ RoleLed.ADMIN })
	List<Account> findAll() throws MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	Account findById(Long id) throws MethodNotAllowedException, NotFoundException;

	Account findByUsername(String username) throws NotFoundException;
}
