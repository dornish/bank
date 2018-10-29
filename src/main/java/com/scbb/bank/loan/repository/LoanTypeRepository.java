package com.scbb.bank.loan.repository;

import com.scbb.bank.loan.model.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTypeRepository extends JpaRepository<LoanType, Integer> {
}
