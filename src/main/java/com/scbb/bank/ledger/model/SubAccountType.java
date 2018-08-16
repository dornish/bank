package com.scbb.bank.ledger.model;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Account> accountList = new ArrayList<>();
}
