package com.scbb.bank.ledger.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubAccountTypeReport {

	private String name;
	private BigDecimal balance;

	public SubAccountTypeReport(String name) {
		this.name = name;
	}
}
