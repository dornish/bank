package com.scbb.bank.savings.model;

import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.person.model.Member;
import com.scbb.bank.savings.model.enums.SavingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Savings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate openedDate;

	@Enumerated(EnumType.STRING)
	private SavingStatus savingStatus;

	@ManyToOne
	private SavingType savingType;

	@OneToOne(cascade = CascadeType.PERSIST)
	private Account account;

	@ManyToOne
	private Member member;
}
