package com.scbb.bank.loan.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentScheduleResponse {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigDecimal interest;
    private BigDecimal total;
    private BigDecimal principal;

}
