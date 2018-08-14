package com.scbb.bank.account.repository;

import com.scbb.bank.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findFirstByNumberStartsWithOrderByIdDesc(String number);
}
