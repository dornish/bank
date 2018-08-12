package com.scbb.bank.account.controller;

import com.scbb.bank.account.model.Account;
import com.scbb.bank.account.service.AccountService;
import com.scbb.bank.person.controller.AbstractController;
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
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(modifyResources(accountService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Account> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(accountService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Account> persist(@RequestBody Account account) {
        return ResponseEntity.ok(modifyResource(accountService.persist(account)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Account>> search(Account account) {
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
