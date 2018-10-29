package com.scbb.bank.ledger.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.model.Transaction;
import com.scbb.bank.ledger.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("transactions")
public class TransactionController implements AbstractController<Transaction, Integer> {

	private TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping
	public List<Transaction> findAll() {
		return modifyResources(transactionService.findAll());
	}

	@GetMapping("{id}")
	public Transaction findById(@PathVariable Integer id) {
		return null;
	}

	@GetMapping("entries/accounts/number/{number}")
	public List<Transaction> findAllByEntryAccountNumber(@PathVariable String number) {
		return modifyResources(transactionService.findAllByEntryAccountNumber(number));
	}

	@PostMapping
	public Transaction persist(@RequestBody Transaction transaction) {
		return modifyResource(transactionService.record(transaction));
	}

	@PostMapping("reverse/{id}")
	public Transaction reverse(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
		return modifyResource(transactionService.reverse(id, token));
	}

	@Override
	public ResponseEntity<String> delete(Integer id) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Transactions cannot be deleted");
	}

	@Override
	public List<Transaction> search(Transaction transaction) {
		return null;
	}

	@Override
	public Transaction modifyResource(Transaction transaction) {
		if (transaction.getEntryList() != null) {
			transaction.getEntryList().forEach(entry -> {
				entry.setTransaction(null);

				Integer id = entry.getAccount().getId();
				entry.setAccount(null);
				entry.setAccount(new Account(id));

			});
		}
		if (transaction.getUser() != null) {
			transaction.getUser().setBoardMember(null);
			transaction.getUser().setStaff(null);
			transaction.getUser().setAuthorities(null);
		}
		return transaction;
	}

	@Override
	public List<Transaction> modifyResources(List<Transaction> transactions) {
		return transactions.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
