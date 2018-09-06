package com.scbb.bank.ledger.controller;


import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.service.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("entries")
public class EntryController implements AbstractController<Entry, Integer> {


    private EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    public List<Entry> findAll() {
        return modifyResources(entryService.findAll());
    }

    @GetMapping("account/number/{number}")
    public List<Entry> findTop3ByAccountNumber(@PathVariable String number) {
        return modifyResources(entryService.findTop3ByAccountNumber(number));
    }

    @GetMapping("{id}")
    public Entry findById(@PathVariable Integer id) {
        return modifyResource(entryService.findById(id));
    }

    @PostMapping
    public Entry persist(@RequestBody Entry entry) {
        return modifyResource(entryService.persist(entry));
    }

    @Override
    public ResponseEntity delete(Integer id) {
        return null;
    }

    @PutMapping("search")
    public List<Entry> search(Entry entry) {
        return modifyResources(entryService.search(entry));
    }

    @Override
    public Entry modifyResource(Entry entry) {
        if (entry.getAccount() != null) {
            entry.getAccount().setTeam(null);
            entry.getAccount().setSubAccountType(null);
            entry.getAccount().setAccountType(null);
            entry.getAccount().setShareHolder(null);
            entry.getAccount().setSavings(null);
            entry.getAccount().setLoan(null);
        }
        if (entry.getTransaction() != null) {
            entry.getTransaction().setEntryList(null);
            entry.getTransaction().setUser(null);
        }
        return entry;
    }

    @Override
    public List<Entry> modifyResources(List<Entry> entries) {
        return entries.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
