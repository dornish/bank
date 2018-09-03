package com.scbb.bank.ledger.service;


import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntryService implements AbstractService<Entry, Integer> {

    private EntryRepository entryRepository;


    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }


    @Transactional
    public List<Entry> findAll() {
        return entryRepository.findAll();
    }

    @Transactional
    public List<Entry> findTop3ByAccountNumber(String number) {
        return entryRepository.findTop3ByAccountNumberOrderByTransactionDateTimeDesc(number);
    }

    @Transactional
    public Entry findById(Integer id) {
        return entryRepository.getOne(id);
    }

    @Transactional
    public Entry persist(Entry entry) {
        return entryRepository.save(entry);
    }

    @Transactional
    public boolean delete(Integer id) {
        return false;
    }

    @Transactional
    public List<Entry> search(Entry entry) {
        return null;
    }
}
