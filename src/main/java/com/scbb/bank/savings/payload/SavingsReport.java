package com.scbb.bank.savings.payload;

import com.scbb.bank.loan.payload.report.DataSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingsReport {

	private List<DataSet> dataSetList = new ArrayList<>();
	private List<String> labelList = new ArrayList<>();

}
