package io.andersori.led.api.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.policy.Restrictions;

@Service
public interface AccountService {

	@Restrictions({ RoleLed.DEFAULT })
	Account save(Account account) throws ForbiddenExecutionException, UnprocessableEntityException, ConflictException, MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	Account update(Long id, Account account)
			throws UnprocessableEntityException, ConflictException, NotFoundException, MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	void delete(Long id) throws MethodNotAllowedException, NotFoundException;

	@Restrictions({ RoleLed.ADMIN })
	List<Account> findAll() throws MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	List<Account> findAll(int pageNumber, int size) throws MethodNotAllowedException;

	@Restrictions({ RoleLed.ADMIN })
	Account findById(Long id) throws MethodNotAllowedException, NotFoundException;

	@Restrictions({ RoleLed.ADMIN })
	Account findByUsername(String username) throws MethodNotAllowedException, NotFoundException;
}
