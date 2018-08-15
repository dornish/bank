package com.scbb.bank.account.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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
}
