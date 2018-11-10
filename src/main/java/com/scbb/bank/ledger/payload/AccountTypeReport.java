package com.scbb.bank.ledger.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeReport {

	private String name;
	private List<SubAccountTypeReport> subList = new ArrayList<>();
	private BigDecimal balance;

	public AccountTypeReport(String name) {
		this.name = name;
	}
}
