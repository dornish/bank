package com.scbb.bank.loan.repository;

import com.scbb.bank.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

	List<Loan> findAllByMemberIdAndIsApprovedIsTrue(Integer id);
}
