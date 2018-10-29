package com.scbb.bank.loan.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatusRequest {

	private String accountNumber;
	private BigDecimal amount;
}
