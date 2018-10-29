package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	Optional<Account> findFirstByNumberStartsWithOrderByIdDesc(String number);

	Optional<Account> findByNumber(String number);
}
