package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.SubAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubAccountTypeRepository extends JpaRepository<SubAccountType, Integer> {
}
