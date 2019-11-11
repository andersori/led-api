package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;

@Service
public interface AccountService {

	Account save(Account account) throws UnprocessableEntityException;

	Account update(long id, Account account);

	void delete(long id);

	List<Account> findAll();

	Optional<Account> findById(long id);
}
