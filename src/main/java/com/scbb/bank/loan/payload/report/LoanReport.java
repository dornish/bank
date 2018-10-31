package com.scbb.bank.loan.payload.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoanReport {

	@JsonFormat(pattern = "MMM")
	private List<LocalDate> monthList;
	private DataSet emiData;
	private DataSet paidData;

	public LoanReport() {
		monthList = new ArrayList<>();
		emiData = new DataSet();
		paidData = new DataSet();
	}
}

