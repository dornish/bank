package com.scbb.bank.savings.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.savings.model.Savings;
import com.scbb.bank.savings.service.SavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("savings")
public class SavingsController implements AbstractController<Savings, Integer> {

	private SavingsService savingsService;

	public SavingsController(SavingsService savingsService) {
		this.savingsService = savingsService;
	}

	@GetMapping
	public List<Savings> findAll() {
		return modifyResources(savingsService.findAll());
	}

	@GetMapping("members/{id}")
	public List<Savings> findAllByMemberId(@PathVariable Integer id) {
		return modifyResources(savingsService.findAllByMemberId(id));
	}

	@GetMapping("{id}")
	public Savings findById(@PathVariable Integer id) {
		return modifyResource(savingsService.findById(id));
	}

	@PostMapping
	@PutMapping
	public Savings persist(@RequestBody Savings savings) {
		return modifyResource(savingsService.persist(savings));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		savingsService.delete(id);
		return ResponseEntity.ok("Successfully deleted Resource with having id: " + id);
	}

	@PutMapping("search")
	public List<Savings> search(@RequestBody Savings savings) {
		return modifyResources(savingsService.search(savings));
	}

	@Override
	public Savings modifyResource(Savings savings) {
		if (savings.getAccount() != null) {
			savings.getAccount().setShareHolder(null);
			savings.getAccount().setAccountType(null);
			savings.getAccount().setSubAccountType(null);
			savings.getAccount().setTeam(null);
			savings.getAccount().setSavings(null);
			savings.getAccount().setLoan(null);
		}
		if (savings.getMember() != null) {
			savings.getMember().setShareAccount(null);
			savings.getMember().setTeam(null);
			savings.getMember().setBoardMember(null);
			savings.getMember().setSubsidy(null);
			savings.getMember().setSavingsList(null);
			savings.getMember().setLoanList(null);
		}
		return savings;
	}

	@Override
	public List<Savings> modifyResources(List<Savings> savings) {
		return savings.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
