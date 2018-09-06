package com.scbb.bank.ledger.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.ledger.model.AccountType;
import com.scbb.bank.ledger.service.AccountTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("accountTypes")
public class AccountTypeController implements AbstractController<AccountType, Integer> {

    private AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @GetMapping
    public List<AccountType> findAll() {
        return modifyResources(accountTypeService.findAll());
    }

    @GetMapping("{id}")
    public AccountType findById(@PathVariable Integer id) {
        return modifyResource(accountTypeService.findById(id));
    }

    @Override
    public AccountType persist(AccountType accountType) {
        return null;
    }

    @Override
    public ResponseEntity delete(Integer id) {
        return null;
    }

    @Override
    public List<AccountType> search(AccountType accountType) {
        return null;
    }

    @Override
    public AccountType modifyResource(AccountType accountType) {
        if (!accountType.getAccountList().isEmpty()) {
            accountType.getAccountList().forEach(account -> {
                account.setShareHolder(null);
                account.setAccountType(null);
                account.setSubAccountType(null);
                account.setTeam(null);
                account.setSavings(null);
                account.setLoan(null);
            });
        }
        if (!accountType.getSubAccountTypeList().isEmpty()) {
            accountType.getSubAccountTypeList().forEach(subAccountType -> {
                subAccountType.setAccountList(null);
                subAccountType.setAccountType(null);
            });
        }
        return accountType;
    }

    @Override
    public List<AccountType> modifyResources(List<AccountType> accountTypes) {
        return accountTypes.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
