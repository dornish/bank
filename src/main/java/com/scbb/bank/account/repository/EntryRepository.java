package com.scbb.bank.account.repository;

import com.scbb.bank.account.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
}
