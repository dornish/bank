package com.scbb.bank.loan.payload.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataSet {

	List<BigDecimal> data;
	String label;

	public DataSet() {
		data = new ArrayList<>();
	}
}
