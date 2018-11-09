package com.scbb.bank.savings.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
public class SavingType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(precision = 5, scale = 2)
	private BigDecimal interestRate;

	@OneToMany(mappedBy = "savingType")
	private List<Savings> savingsList;

}
