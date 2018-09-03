package com.scbb.bank.loan.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatusResponse {

    private BigDecimal interest;
    private BigDecimal fine;
    private BigDecimal principal;
}
