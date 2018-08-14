package com.scbb.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.account.model.enums.OperationType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String number;

    private String name;
    private Float balance;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedDateTime;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    private AccountType accountType;

    @ManyToOne
    private SubAccountType subAccountType;

    public void credit(Float amount) {
        setBalance(operationType == OperationType.Credit ? getBalance() + amount : getBalance() - amount);
        setLastUpdatedDateTime(LocalDateTime.now());
    }

    public void debit(Float amount) {
        setBalance(operationType == OperationType.Debit ? getBalance() + amount : getBalance() - amount);
        setLastUpdatedDateTime(LocalDateTime.now());
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
