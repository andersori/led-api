package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.policy.Restrictions;

@Service
public interface AccountService {

	@Restrictions({ RoleLed.ADMIN, RoleLed.TEAM })
	Account save(Account account) throws UnprocessableEntityException, ConflictException, ForbiddenExecutionException;

	void delete(long id);

	@Restrictions({ RoleLed.ADMIN, RoleLed.TEAM })
	List<Account> findAll() throws ForbiddenExecutionException;

	@Restrictions({ RoleLed.ADMIN })
	Optional<Account> findById(long id);
}
