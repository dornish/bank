package com.scbb.bank.loan.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class LoanType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(scale = 2, precision = 4)
	private BigDecimal interestRate;

	private Integer minPeriod;

	private Integer maxPeriod;

	@Column(scale = 2, precision = 12)
	private BigDecimal minAmount;

	@Column(scale = 2, precision = 12)
	private BigDecimal maxAmount;
}
