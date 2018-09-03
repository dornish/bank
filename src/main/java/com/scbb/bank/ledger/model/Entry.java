package com.scbb.bank.ledger.model;

import com.scbb.bank.ledger.model.enums.OperationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    private Account account;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    private Transaction transaction;

    public Entry(Account account, BigDecimal amount, Transaction transaction) {
        this.account = account;
        this.amount = amount;
        this.transaction = transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return id != null ? id.equals(entry.id) : entry.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
