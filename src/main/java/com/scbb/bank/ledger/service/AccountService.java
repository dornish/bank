package com.scbb.bank.ledger.service;

import com.scbb.bank.exception.ResourceCannotDeleteException;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.repository.SubAccountTypeRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
		return accountRepository.findAll(Sort.by(Sort.Direction.ASC, "number"));
	}

	@Transactional
	public Account findById(Integer id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account having id " + id + " cannot find"));
	}

	public Account findByNumber(String number) {
		return accountRepository.findByNumber(number)
				.orElseThrow(() -> new ResourceNotFoundException("Account having number " + number + " cannot find"));
	}

	@Transactional
	public Account persist(Account account) {
		if (account.getId() == null) {
			account.setBalance(new BigDecimal("0"));
			account.setNumber(calculateAccountNumber(account));
		} else {
			account.setNumber(accountRepository.getOne(account.getId()).getNumber());
			account.setBalance(accountRepository.getOne(account.getId()).getBalance());
		}
		return accountRepository.save(account);
	}

	@Transactional
	public void delete(Integer id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account having id" + id + " cannot find"));
		if (account.getBalance().compareTo(new BigDecimal("0")) != 0)
			throw new ResourceCannotDeleteException("Account having id " + id + " cannot be deleted");
		accountRepository.delete(account);
	}

	@Transactional
	public List<Account> search(Account account) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withMatcher("number", ExampleMatcher.GenericPropertyMatcher::startsWith);
		return accountRepository.findAll(Example.of(account, matcher), Sort.by(Sort.Direction.ASC, "number"));
	}

	private String calculateAccountNumber(Account account) {
		String subAccountTypeNumber = subAccountTypeRepository.getOne(account.getSubAccountType().getId()).getNumber();
		String number = account.getAccountType().getId().toString() + subAccountTypeNumber;
		if (accountRepository.findFirstByNumberStartsWithOrderByIdDesc(number).isPresent()) {
			Account latestAccount = accountRepository.findFirstByNumberStartsWithOrderByIdDesc(number).get();
			String value = latestAccount.getNumber().substring(2);
			if (Integer.valueOf(value, 10) < 9)
				number += "00" + (Integer.valueOf(value, 10) + 1);
			else if (Integer.valueOf(value, 10) < 99)
				number += "0" + (Integer.valueOf(value, 10) + 1);
			else
				number += Integer.valueOf(value, 10) + 1;
		} else {
			number += "000";
		}
		return number;
	}
}
