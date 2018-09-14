package com.scbb.bank.ledger.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.area.model.Team;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.person.model.Member;
import com.scbb.bank.savings.model.Savings;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String number;

    private String name;

    @Column(precision = 12, scale = 2)
    private BigDecimal balance;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedDateTime;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    private AccountType accountType;

    @ManyToOne
    private SubAccountType subAccountType;

    @OneToOne(mappedBy = "shareAccount")
    private Member shareHolder;

    @OneToOne(mappedBy = "account")
    private Savings savings;

    @OneToOne(mappedBy = "account")
    private Team team;

    @OneToOne(mappedBy = "account")
    private Loan loan;

    public void credit(BigDecimal amount) {
        if (operationType == OperationType.Credit) setBalance(getBalance().add(amount));
        else setBalance(getBalance().subtract(amount));
        setLastUpdatedDateTime(LocalDateTime.now());
    }

    public void debit(BigDecimal amount) {
        if (operationType == OperationType.Debit) setBalance(getBalance().add(amount));
        else setBalance(getBalance().subtract(amount));
        setLastUpdatedDateTime(LocalDateTime.now());
    }

    public Account(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id != null ? id.equals(account.id) : account.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
