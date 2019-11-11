package io.andersori.led.api.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.andersori.led.api.domain.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	
}
