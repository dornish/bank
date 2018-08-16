package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
}
