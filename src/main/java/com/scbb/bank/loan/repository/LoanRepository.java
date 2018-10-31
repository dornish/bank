package com.scbb.bank.loan.repository;

import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.model.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

	List<Loan> findAllByMemberIdAndLoanStatus(Integer id, LoanStatus loanStatus);
}
