package com.scbb.bank.ledger.service;


import com.scbb.bank.authentication.security.JwtTokenProvider;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.Transaction;
import com.scbb.bank.ledger.model.enums.EntryType;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.repository.TransactionRepository;
import com.scbb.bank.person.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;
	private JwtTokenProvider tokenProvider;

	public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, JwtTokenProvider tokenProvider) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
		this.tokenProvider = tokenProvider;
	}

	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	public List<Transaction> findAllByEntryAccountNumber(String number) {
		return transactionRepository.findAllByEntryListAccountNumberOrderByDateTimeDesc(number);
	}

	public List<Transaction> findAllByEntryAccountNumber(String number, LocalDateTime fromDate, LocalDateTime toDate) {
		return transactionRepository.findAllByEntryListAccountNumberAndDateTimeBetweenOrderByDateTimeDesc(number, fromDate, toDate);
	}

	public Transaction findById(Integer id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("transaction having id " + id + "cannot find"));
	}

	public List<Transaction> search(Transaction transaction) {
		return null;
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
	public Transaction reverse(Integer id, String token) {
		Integer userId = tokenProvider.getUserIdFromToken(token.substring(7));
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("transaction having id " + id + " cannot find"));
		Transaction newTransaction = new Transaction(LocalDateTime.now(), EntryType.Adjusting_Entry, new User(userId));
		transaction.getEntryList()
				.forEach(entry -> {
					Account account = accountRepository.getOne(entry.getAccount().getId());
					Entry newEntry = new Entry(account, entry.getAmount(), newTransaction);
					if (entry.getOperationType() == OperationType.Credit) {
						account.debit(entry.getAmount());
						newEntry.setOperationType(OperationType.Debit);
					} else {
						account.credit(entry.getAmount());
						newEntry.setOperationType(OperationType.Credit);
					}
					accountRepository.save(account);
					newTransaction.getEntryList().add(newEntry);
				});
		return transactionRepository.save(newTransaction);
	}
}
