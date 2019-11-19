package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.util.AccountContext;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.exception.ConflictException;
import io.andersori.led.api.domain.exception.ForbiddenExecutionException;
import io.andersori.led.api.domain.exception.MethodNotAllowedException;
import io.andersori.led.api.domain.exception.NotFoundException;
import io.andersori.led.api.domain.exception.UnprocessableEntityException;
import io.andersori.led.api.domain.util.ValidatorEntity;
import io.andersori.led.api.resource.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {

	private final AccountRepository repository;

	@Autowired
	public AccountServiceImp(AccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public Account save(Account account) throws ForbiddenExecutionException, UnprocessableEntityException,
			ConflictException, MethodNotAllowedException {

		ValidatorEntity.validate(account, Account.class);

		if (!(account.getRoles().contains(RoleLed.ADMIN)
				&& !AccountContext.getAccount().getRoles().contains(RoleLed.ADMIN))) {
			if (!repository.findByUsername(account.getUsername()).isPresent()) {
				repository.save(account);
			} else {
				throw new ConflictException("The username you entered is alread in use.", Account.class);
			}
		} else {
			throw new ForbiddenExecutionException(
					"You do not have permission to create a user with administrator permission", Account.class);
		}

		return account;
	}

	@Override
	public Account update(Long id, Account account)
			throws UnprocessableEntityException, ConflictException, NotFoundException, MethodNotAllowedException {

		Account acSaved = findById(id);

		if (account.getName() != null && !account.getName().equals(acSaved.getName())) {
			acSaved.setName(account.getName());
		}

		if (account.getUsername() != null && !account.getUsername().equals(acSaved.getUsername())) {
			if (repository.findByUsername(account.getUsername()).isPresent()) {
				throw new ConflictException("The username you entered is alread in use.", Account.class);
			}
			acSaved.setUsername(account.getUsername());
		}

		if (!account.getRoles().containsAll(acSaved.getRoles())) {
			acSaved.setRoles(account.getRoles());
		}

		if (account.getPassword() != null) {
			acSaved.setPassword(account.getPassword());
		}

		ValidatorEntity.validate(acSaved, Account.class);
		repository.save(acSaved);

		return acSaved;
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
	public List<Account> findAll(int pageNumber, int size) throws MethodNotAllowedException {
		Pageable page = PageRequest.of(pageNumber, size, Sort.by("accountId"));
		return repository.findAll(page).getContent();
	}

	@Override
	public Account findById(Long id) throws MethodNotAllowedException, NotFoundException {
		Optional<Account> ac = repository.findById(id);
		if (ac.isPresent()) {
			return ac.get();
		}
		throw new NotFoundException(Account.class);
	}

	@Override
	public Account findByUsername(String username) throws MethodNotAllowedException, NotFoundException {
		Optional<Account> ac = repository.findByUsername(username);
		if (ac.isPresent()) {
			return ac.get();
		}
		throw new NotFoundException(Account.class);
	}

}
