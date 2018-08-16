package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findFirstByNumberStartsWithOrderByIdDesc(String number);
}
