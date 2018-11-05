package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

	List<Entry> findTop3ByAccountNumberOrderByTransactionDateTimeDesc(String number);

	List<Entry> findAllByTransactionDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
