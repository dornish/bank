package com.scbb.bank.account.repository;

import com.scbb.bank.account.model.SubAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubAccountTypeRepository extends JpaRepository<SubAccountType, Integer> {
}
