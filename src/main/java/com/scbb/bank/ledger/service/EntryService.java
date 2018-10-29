package com.scbb.bank.ledger.service;


import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.repository.EntryRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
		return entryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entry having id " + id + " cannot find"));
	}

	@Transactional
	public Entry persist(Entry entry) {
		return entryRepository.save(entry);
	}

	@Transactional
	public void delete(Integer id) {
	}

	@Transactional
	public List<Entry> search(Entry entry) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		return entryRepository.findAll(Example.of(entry, matcher));
	}
}
