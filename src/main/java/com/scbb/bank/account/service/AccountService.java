package com.scbb.bank.account.service;

import com.scbb.bank.account.model.Account;
import com.scbb.bank.account.repository.AccountRepository;
import com.scbb.bank.person.service.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements AbstractService<Account, Integer> {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Integer id) {
        return accountRepository.getOne(id);
    }

    @Override
    public Account persist(Account account) {
        if (account.getId() == null) account.setBalance(0F);
        return accountRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    public List<Account> search(Account account) {
        return null;
    }
}
