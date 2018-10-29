package com.scbb.bank.ledger.model;

import com.scbb.bank.ledger.model.enums.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	private Account account;

	@Column(precision = 12, scale = 2)
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

	public Entry(Account account, BigDecimal amount, OperationType operationType) {
		this.account = account;
		this.amount = amount;
		this.operationType = operationType;
	}

	public Entry(BigDecimal amount, OperationType operationType) {
		this.amount = amount;
		this.operationType = operationType;
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
