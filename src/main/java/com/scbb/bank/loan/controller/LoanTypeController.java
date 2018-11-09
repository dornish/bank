package com.scbb.bank.loan.controller;


import com.scbb.bank.loan.model.LoanType;
import com.scbb.bank.loan.service.LoanTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("loanTypes")
public class LoanTypeController{

	private LoanTypeService loanTypeService;

	public LoanTypeController(LoanTypeService loanTypeService) {
		this.loanTypeService = loanTypeService;
	}


	@GetMapping
	public List<LoanType> findAll() {
		return modifyResources(loanTypeService.findAll());
	}

	@GetMapping("{id}")
	public LoanType findById(@PathVariable Integer id) {
		return loanTypeService.findById(id);
	}

	@PostMapping
	@PutMapping
	public LoanType persist(@RequestBody LoanType loanType) {
		return loanTypeService.persist(loanType);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		loanTypeService.delete(id);
		return ResponseEntity.ok("Successfully deleted loan type with having id: " + id);
	}

	@PutMapping("search")
	public List<LoanType> search(
			@RequestBody LoanType loanType,
			@RequestParam(required = false) BigDecimal amount,
			@RequestParam(required = false) Integer period
	)
	{
		return loanTypeService.search(loanType, amount, period);
	}


	public LoanType modifyResource(LoanType loanType) {
		if (loanType.getLoanList() != null) {
			loanType.getLoanList().forEach(loan -> {
				loan.setAccount(null);
				loan.setMember(null);
				loan.setLoanType(null);
			});
		}
		return loanType;
	}


	public List<LoanType> modifyResources(List<LoanType> loanTypes) {
		return loanTypes.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
