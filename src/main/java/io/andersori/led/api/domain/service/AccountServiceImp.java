package io.andersori.led.api.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
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
	public Account save(Account account) throws UnprocessableEntityException, ConflictException {
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

			throw new UnprocessableEntityException("Account Validation Error.", Account.class, errors);
		}

		if (!repository.findByUsername(account.getUsername()).isPresent()) {
			repository.save(account);
		} else {
			ErrorInfo error = new ErrorInfo("Username must be unique.", "username", account.getUsername());
			
			throw new ConflictException("The username you entered is alread in use.", Account.class, Arrays.asList(error));
		}

		return account;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Account> findAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Account> findById(long id) {
		return repository.findById(id);
	}

}
