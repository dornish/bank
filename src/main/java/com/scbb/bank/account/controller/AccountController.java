package com.scbb.bank.account.controller;

import com.scbb.bank.account.model.Account;
import com.scbb.bank.account.service.AccountService;
import com.scbb.bank.interfaces.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("accounts")
public class AccountController implements AbstractController<Account, Integer> {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> findAll() {
        return modifyResources(accountService.findAll());
    }

    @GetMapping("{id}")
    public Account findById(@PathVariable Integer id) {
        return modifyResource(accountService.findById(id));
    }

    @PostMapping
    public Account persist(@RequestBody Account account) {
        return modifyResource(accountService.persist(account));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("search")
    public List<Account> search(@RequestBody Account account) {
        return null;
    }

    @Override
    public Account modifyResource(Account account) {
        return account;
    }

    @Override
    public List<Account> modifyResources(List<Account> accounts) {
        return accounts.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
