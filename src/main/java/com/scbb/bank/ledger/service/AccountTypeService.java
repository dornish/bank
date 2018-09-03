package com.scbb.bank.ledger.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.AccountType;
import com.scbb.bank.ledger.repository.AccountTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountTypeService implements AbstractService<AccountType, Integer> {

    private AccountTypeRepository accountTypeRepository;

    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Transactional
    public List<AccountType> findAll() {
        return accountTypeRepository.findAll();
    }

    @Transactional
    public AccountType findById(Integer id) {
        return accountTypeRepository.getOne(id);
    }

    @Override
    public AccountType persist(AccountType accountType) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<AccountType> search(AccountType accountType) {
        return null;
    }
}
