package io.andersori.led.api.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.error.ApiError;
import io.andersori.led.api.domain.error.ApiSubError;
import io.andersori.led.api.domain.error.ApiValidationError;
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
	public Account save(Account account) throws UnprocessableEntityException {
		Set<ConstraintViolation<Account>> violations = validator.validate(account);
		if (!violations.isEmpty()) {
			List<ApiSubError> errors = new ArrayList<>();

			for (ConstraintViolation<Account> violation : violations) {
				ApiValidationError error = new ApiValidationError();
				error.setField(violation.getPropertyPath().toString());
				error.setMessage(violation.getMessage());
				error.setObject("account");
				error.setRejectedValue(violation.getInvalidValue());

				errors.add(error);
			}

			throw new UnprocessableEntityException(
					new ApiError(HttpStatus.Code.BAD_REQUEST, "Account Validation Error!", errors));
		}

		try {
			repository.save(account);
		} catch (Exception e) {
			e.printStackTrace();

			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException ec = (ConstraintViolationException) e.getCause();
				System.out.println(ec.getMessage());
				System.out.println(ec.getConstraintName());
				System.out.println(ec.getLocalizedMessage());
				System.out.println("----------------");
				if(ec.getCause() instanceof PSQLException) {
					PSQLException ep = (PSQLException) ec.getCause();
					System.out.println(ep.getMessage());
				}
			}
			/*
			 * ConstraintViolationException ex = (ConstraintViolationException) e;
			 * List<ConstraintViolationDescription> errors = new ArrayList<>();
			 * 
			 * for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			 * String attribute = violation.getPropertyPath().toString(); String message =
			 * violation.getMessage();
			 * 
			 * errors.add(new ConstraintViolationDescription(attribute, message)); }
			 */
			throw new UnprocessableEntityException(
					new ApiError(HttpStatus.Code.BAD_REQUEST , e.getMessage(), Arrays.asList()));
		}

		return account;
	}

	@Override
	public Account update(long id, Account account) {
		// TODO Auto-generated method stub
		return null;
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
