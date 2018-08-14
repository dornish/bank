package com.scbb.bank.account.service;

import com.scbb.bank.account.model.Account;
import com.scbb.bank.account.repository.AccountRepository;
import com.scbb.bank.account.repository.SubAccountTypeRepository;
import com.scbb.bank.person.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements AbstractService<Account, Integer> {

    private AccountRepository accountRepository;
    private SubAccountTypeRepository subAccountTypeRepository;


    public AccountService(AccountRepository accountRepository, SubAccountTypeRepository subAccountTypeRepository) {
        this.accountRepository = accountRepository;
        this.subAccountTypeRepository = subAccountTypeRepository;
    }

    @Transactional
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account findById(Integer id) {
        return accountRepository.getOne(id);
    }

    @Transactional
    public Account persist(Account account) {
        if (account.getId() == null) {
            account.setBalance(0F);
            account.setNumber(calculateAccountNumber(account));
        }
        return accountRepository.save(account);
    }

    @Transactional
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public List<Account> search(Account account) {
        return null;
    }

    private String calculateAccountNumber(Account account) {
        String subAccountTypeNumber = subAccountTypeRepository.getOne(account.getSubAccountType().getId()).getNumber();
        String number = account.getAccountType().getId().toString() + subAccountTypeNumber;
        Account latestAccount = accountRepository.findFirstByNumberStartsWithOrderByIdDesc(number);
        if (latestAccount != null) {
            String value = latestAccount.getNumber().substring(2);
            if (Integer.valueOf(value, 10) < 9)
                number += "00" + (Integer.valueOf(value, 10) + 1);
            else if (Integer.valueOf(value, 10) < 99)
                number += "0" + (Integer.valueOf(value, 10) + 1);
            else
                number += Integer.valueOf(value, 10) + 1;
        }
        else {
            number += "000";
        }
        return number;
    }
}
