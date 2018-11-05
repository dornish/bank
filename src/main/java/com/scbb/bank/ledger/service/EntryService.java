package com.scbb.bank.ledger.service;


import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.repository.EntryRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService {

	private EntryRepository entryRepository;


	public EntryService(EntryRepository entryRepository) {
		this.entryRepository = entryRepository;
	}


	@Transactional
	public List<Entry> findAll(LocalDateTime fromDate, LocalDateTime toDate) {
		if (fromDate != null && toDate != null) {
			return entryRepository.findAllByTransactionDateTimeBetween(fromDate, toDate);
		}
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
	public List<Entry> search(Entry entry, LocalDateTime fromDate, LocalDateTime toDate) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		List<Entry> entryList = entryRepository.findAll(Example.of(entry, matcher));
		if (fromDate != null && toDate != null)
			return entryList.stream()
					.peek(entry1 -> System.out.println("entering to loop " + entry1.getTransaction().getId() + " --> " + entry1.getTransaction().getDateTime()))
					.filter(entry1 -> entry1.getTransaction().getDateTime().isBefore(toDate))
					.filter(entry1 -> entry1.getTransaction().getDateTime().isAfter(fromDate))
					.peek(entry1 -> System.out.println("filtered " + entry1.getTransaction().getId() + " --> " + entry1.getTransaction().getDateTime()))
					.collect(Collectors.toList());
		return entryList;
	}
}
