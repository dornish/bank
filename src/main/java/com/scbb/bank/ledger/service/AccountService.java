package com.scbb.bank.ledger.service;

import com.scbb.bank.area.repository.TeamRepository;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.repository.SubAccountTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements AbstractService<Account, Integer> {

    private AccountRepository accountRepository;
    private SubAccountTypeRepository subAccountTypeRepository;

    private TeamRepository teamRepository;


    public AccountService(AccountRepository accountRepository, SubAccountTypeRepository subAccountTypeRepository, TeamRepository teamRepository) {
        this.accountRepository = accountRepository;
        this.subAccountTypeRepository = subAccountTypeRepository;
        this.teamRepository = teamRepository;
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
        if (account.getTeam() != null)
            teamRepository.getOne(account.getTeam().getId()).setAccount(account);
        return accountRepository.save(account);
    }

    @Transactional
    public boolean delete(Integer id) {
        Account account = accountRepository.getOne(id);
        if (account.getBalance() == 0F) {
            accountRepository.delete(account);
            return true;
        }
        return false;
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
