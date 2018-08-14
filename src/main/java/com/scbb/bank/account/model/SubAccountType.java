package com.scbb.bank.account.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
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
    private Set<Account> accountSet;
}
