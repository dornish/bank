package com.scbb.bank.ledger.service;


import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.Transaction;
import com.scbb.bank.ledger.model.enums.EntryType;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction record(Transaction transaction) {
        transaction.getEntryList()
                .forEach(entry -> {
                    entry.setTransaction(transaction);
                    Account account = accountRepository.getOne(entry.getAccount().getId());
                    if (entry.getOperationType() == OperationType.Credit)
                        account.credit(entry.getAmount());
                    else
                        account.debit(entry.getAmount());
                    accountRepository.save(account);
                });
        transaction.setDateTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction reverse(Integer id) {
        Transaction transaction = transactionRepository.getOne(id);
        Transaction newTransaction = new Transaction(LocalDateTime.now(), EntryType.Adjusting_Entry, transaction.getUser());
        transaction.getEntryList()
                .forEach(entry -> {
                    Account account = accountRepository.getOne(entry.getAccount().getId());
                    Entry newEntry = new Entry(account, entry.getAmount(), newTransaction);
                    if (entry.getOperationType() == OperationType.Credit) {
                        account.debit(entry.getAmount());
                        newEntry.setOperationType(OperationType.Debit);
                    }
                    else {
                        account.credit(entry.getAmount());
                        newEntry.setOperationType(OperationType.Credit);
                    }
                    accountRepository.save(account);
                    newTransaction.getEntryList().add(newEntry);
                });
        return transactionRepository.save(newTransaction);
    }

    public List<Transaction> findAllByAccountId(String number) {
        return transactionRepository.findAllByEntryListAccountNumber(number);
    }
}
