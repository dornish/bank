package com.scbb.bank.loan.payload.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PieChart {

	List<BigDecimal> dataList = new ArrayList<>();
	List<String> labelList = new ArrayList<>();
}
