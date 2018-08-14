package com.scbb.bank.account.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "accountType")
    private Set<Account> accountSet;

    @OneToMany(mappedBy = "accountType")
    private Set<SubAccountType> subAccountTypeSet;
}
