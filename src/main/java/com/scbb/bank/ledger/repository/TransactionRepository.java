package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByEntryListAccountNumber(String number);
}
