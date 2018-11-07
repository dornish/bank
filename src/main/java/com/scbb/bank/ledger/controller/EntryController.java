package com.scbb.bank.ledger.controller;


import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.service.EntryService;
import com.scbb.bank.person.model.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("entries")
public class EntryController {


	private EntryService entryService;

	public EntryController(EntryService entryService) {
		this.entryService = entryService;
	}

	@GetMapping
	public List<Entry> findAll(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime fromDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime toDate)
	{
		return modifyResources(entryService.findAll(fromDate, toDate));
	}

	@GetMapping("account/number/{number}")
	public List<Entry> findAllByAccountNumber(
			@PathVariable String number,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime fromDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime toDate)
	{
		return modifyResources(entryService.findAllByAccountNumber(number, fromDate, toDate));
	}

	@GetMapping("top5/account/number/{number}")
	public List<Entry> findTop5ByAccountNumber(@PathVariable String number) {
		return modifyResources(entryService.findTop5ByAccountNumber(number));
	}

	@GetMapping("{id}")
	public Entry findById(@PathVariable Integer id) {
		return modifyResource(entryService.findById(id));
	}

	@PostMapping
	public Entry persist(@RequestBody Entry entry) {
		return modifyResource(entryService.persist(entry));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("entries cannot be deleted");
	}

	@PutMapping("search")
	public List<Entry> search(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime fromDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime toDate,
			@RequestBody Entry entry
	) {
		System.out.println("from " + fromDate);
		System.out.println("to " + toDate);
		return modifyResources(entryService.search(entry, fromDate, toDate));
	}

	public Entry modifyResource(Entry entry) {
		if (entry.getAccount() != null) {
			entry.getAccount().setTeam(null);
			entry.getAccount().setSubAccountType(null);
			entry.getAccount().setAccountType(null);
			entry.getAccount().setShareHolder(null);
			entry.getAccount().setSavings(null);
			entry.getAccount().setLoan(null);
		}
		if (entry.getTransaction() != null) {
			entry.getTransaction().setEntryList(null);
			String username = entry.getTransaction().getUser().getUsername();
			entry.getTransaction().setUser(null);
			entry.getTransaction().setUser(new User(username));
		}
		return entry;
	}

	public List<Entry> modifyResources(List<Entry> entries) {
		return entries.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
