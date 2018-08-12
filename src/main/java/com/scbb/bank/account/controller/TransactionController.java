package com.scbb.bank.account.controller;

import com.scbb.bank.account.model.Transaction;
import com.scbb.bank.account.service.TransactionService;
import com.scbb.bank.person.controller.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("transactions")
public class TransactionController implements AbstractController<Transaction, Integer> {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll() {
        return ResponseEntity.ok(modifyResources(transactionService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Transaction> findById(@PathVariable Integer id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Transaction> persist(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(modifyResource(transactionService.recordTransaction(transaction)));
    }

    @Override
    public ResponseEntity delete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Transaction>> search(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction modifyResource(Transaction transaction) {
        if (!transaction.getEntryList().isEmpty()) {
            transaction.getEntryList().forEach(entry ->{
                entry.setTransaction(null);
                entry.setAccount(null);
            });
        }
        if (transaction.getUser() != null) {
            transaction.getUser().setBoardMember(null);
            transaction.getUser().setStaff(null);
            transaction.getUser().setAuthorities(null);
        }
        return transaction;
    }

    @Override
    public List<Transaction> modifyResources(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
