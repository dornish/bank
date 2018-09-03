package com.scbb.bank.loan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.person.model.Member;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 12, scale = 2)
    private BigDecimal requestedAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate grantedDate;

    private Integer duration;

    private Integer remainingInstallments;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextInstallmentDate;

    private String remarks;

    private Boolean isApproved;

    @ManyToOne
    private LoanType loanType;

    @OneToOne
    private Account account;

    @ManyToOne
    private Member member;


}
