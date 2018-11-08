package com.scbb.bank.savings.service;

import com.scbb.bank.exception.ResourceCannotDeleteException;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.service.AccountService;
import com.scbb.bank.savings.model.Savings;
import com.scbb.bank.savings.repository.SavingsRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SavingsService implements AbstractService<Savings, Integer> {

	private SavingsRepository savingsRepository;
	private AccountService accountService;

	public SavingsService(SavingsRepository savingsRepository, AccountService accountService) {
		this.savingsRepository = savingsRepository;
		this.accountService = accountService;
	}

	@Transactional
	public List<Savings> findAll() {
		return savingsRepository.findAll();
	}

	@Transactional
	public List<Savings> findAllByMemberId(Integer id) {
		return savingsRepository.findAllByMemberId(id);
	}

	@Transactional
	public Savings findById(Integer id) {
		return savingsRepository.getOne(id);
	}

	@Transactional
	public Savings persist(Savings savings) {
		if (savings.getId() == null) {
			accountService.persist(savings.getAccount());
			savings.setOpenedDate(LocalDate.now());
		}
		return savingsRepository.save(savings);
	}

	@Transactional
	public void delete(Integer id) {
		Savings savings = savingsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
		if (savings.getAccount().getBalance().compareTo(new BigDecimal("0")) != 0) {
			throw new ResourceCannotDeleteException("Savings having id " + id + " cannot be deleted");
		}
		savingsRepository.deleteById(id);
	}

	@Transactional
	public List<Savings> search(Savings savings) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

		Example<Savings> example = Example.of(savings, matcher);

		return savingsRepository.findAll(example);
	}
}
