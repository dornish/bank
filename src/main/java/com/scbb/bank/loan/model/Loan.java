package com.scbb.bank.loan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.loan.model.enums.LoanStatus;
import com.scbb.bank.person.model.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(precision = 12, scale = 2)
	private BigDecimal requestedAmount;

	@Column(precision = 12, scale = 2)
	private BigDecimal equatedMonthlyValue;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate requestedDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate grantedDate;

	private Integer duration;

	private String remarks;

	@Enumerated(EnumType.STRING)
	private LoanStatus loanStatus;

	@ManyToOne
	private LoanType loanType;

	@OneToOne
	private Account account;

	@ManyToOne
	private Member member;


}
