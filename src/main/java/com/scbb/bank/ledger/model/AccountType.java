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
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "accountType")
	private List<Account> accountList = new ArrayList<>();

	@OneToMany(mappedBy = "accountType")
	private List<SubAccountType> subAccountTypeList = new ArrayList<>();

	public AccountType(Integer id) {
		setId(id);
	}
}
