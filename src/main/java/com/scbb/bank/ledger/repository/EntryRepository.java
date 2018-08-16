package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
}
