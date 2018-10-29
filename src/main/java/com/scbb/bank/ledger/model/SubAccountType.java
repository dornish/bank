package com.scbb.bank.ledger.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubAccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String number;

	private String name;

	@ManyToOne
	private AccountType accountType;

	@OneToMany(mappedBy = "subAccountType")
	private List<Account> accountList = new ArrayList<>();

	public SubAccountType(Integer id, String number) {
		setId(id);
		setNumber(number);
	}
}
