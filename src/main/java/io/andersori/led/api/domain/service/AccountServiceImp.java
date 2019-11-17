package io.andersori.led.api.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.error.ErrorInfo;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.resource.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {

	private final AccountRepository repository;
	private final Validator validator;

	@Autowired
	public AccountServiceImp(AccountRepository repository, Validator validator) {
		this.repository = repository;
		this.validator = validator;
	}

	@Override
	public Account save(Account account) throws UnprocessableEntityException, ConflictException, MethodNotAllowedException {
		Set<ConstraintViolation<Account>> violations = validator.validate(account);
		if (!violations.isEmpty()) {
			List<ErrorInfo> errors = new ArrayList<>();

			for (ConstraintViolation<Account> violation : violations) {
				ErrorInfo error = new ErrorInfo();
				error.setField(violation.getPropertyPath().toString());
				error.setMessage(violation.getMessage());
				error.setRejectedValue(violation.getInvalidValue());

				errors.add(error);
			}

			throw new UnprocessableEntityException(Account.class, errors);
		}

		if (!repository.findByUsername(account.getUsername()).isPresent()) {
			repository.save(account);
		} else {			
			throw new ConflictException("The username you entered is alread in use.", Account.class);
		}

		return account;
	}

	@Override
	public void delete(Long id) throws MethodNotAllowedException, NotFoundException {
		findById(id);
		repository.deleteById(id);
	}

	@Override
	public List<Account> findAll() throws MethodNotAllowedException {
		return repository.findAll();
	}

	@Override
	public Account findById(Long id) throws MethodNotAllowedException, NotFoundException {
		Optional<Account> ac = repository.findById(id);
		if(ac.isPresent()) {
			return ac.get();
		}
		throw new NotFoundException(Account.class);
	}

	@Override
	public Account findByUsername(String username) throws NotFoundException {
		Optional<Account> ac = repository.findByUsername(username);
		if(ac.isPresent()) {
			return ac.get();
		}
		throw new NotFoundException(Account.class);
	}

}
