package com.scbb.bank.ledger.controller;

import com.scbb.bank.ledger.model.AccountType;
import com.scbb.bank.ledger.service.AccountTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("accountTypes")
public class AccountTypeController {

	private AccountTypeService accountTypeService;

	public AccountTypeController(AccountTypeService accountTypeService) {
		this.accountTypeService = accountTypeService;
	}

	@GetMapping
	public List<AccountType> findAll() {
		return modifyResources(accountTypeService.findAll());
	}

	@GetMapping("{id}")
	public AccountType findById(@PathVariable Integer id) {
		return modifyResource(accountTypeService.findById(id));
	}

	@PutMapping
	@PostMapping
	public ResponseEntity<String> persist(@RequestBody AccountType accountType) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("account types cannot be inserted");
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("account types cannot be deleted");
	}

	public List<AccountType> search(AccountType accountType) {
		return null;
	}

	public AccountType modifyResource(AccountType accountType) {
		if (accountType.getAccountList() != null) {
			accountType.getAccountList().forEach(account -> {
				account.setShareHolder(null);
				account.setAccountType(null);
				account.setSubAccountType(null);
				account.setTeam(null);
				account.setSavings(null);
				account.setLoan(null);
			});
		}
		if (accountType.getSubAccountTypeList() != null) {
			accountType.getSubAccountTypeList().forEach(subAccountType -> {
				subAccountType.setAccountList(null);
				subAccountType.setAccountType(null);
			});
		}
		return accountType;
	}

	public List<AccountType> modifyResources(List<AccountType> accountTypes) {
		return accountTypes.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
