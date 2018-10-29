package com.scbb.bank.loan.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentScheduleRequest {

	private BigDecimal amount;
	private BigDecimal interestRate;
	private Integer duration;

}
