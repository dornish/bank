package com.scbb.bank.loan.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.model.enums.LoanReleaseType;
import com.scbb.bank.loan.payload.InstallmentScheduleRequest;
import com.scbb.bank.loan.payload.InstallmentScheduleResponse;
import com.scbb.bank.loan.payload.LoanStatusRequest;
import com.scbb.bank.loan.payload.LoanStatusResponse;
import com.scbb.bank.loan.payload.report.LoanReport;
import com.scbb.bank.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("loans")
public class LoanController implements AbstractController<Loan, Integer> {

	private LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@GetMapping
	public List<Loan> findAll() {
		return modifyResources(loanService.findAll());
	}

	@GetMapping("members/{id}")
	public List<Loan> findAllByMemberId(@PathVariable Integer id) {
		return modifyResources(loanService.findAllByMemberId(id));
	}

	@GetMapping("{id}")
	public Loan findById(@PathVariable Integer id) {
		return modifyResource(loanService.findById(id));
	}

	@GetMapping("nextInstallmentAmount/{id}")
	public BigDecimal nextInstallmentAmount(@PathVariable Integer id) {
		return loanService.nextInstallmentAmount(id);
	}

	@GetMapping("calcArrears/{id}")
	public BigDecimal calculateArrears(@PathVariable Integer id) {
		return loanService.calculateArrears(id);
	}

	@GetMapping("calcRemaining/{id}")
	public BigDecimal calculateRemaining(@PathVariable Integer id) {
		return loanService.calculateRemainingAmount(id);
	}

	@GetMapping("report/{id}")
	public LoanReport report(@PathVariable Integer id) {
		return loanService.report(id);
	}

	@PutMapping("calcInterest")
	public LoanStatusResponse calculateInterest(@RequestBody LoanStatusRequest loanStatusRequest) {
		return loanService.calculateInterest(loanStatusRequest);
	}

	@PutMapping("calcSchedule")
	public List<InstallmentScheduleResponse> calculateInstallmentSchedule(@RequestBody InstallmentScheduleRequest request) {
		return loanService.calculateInstallmentSchedule(request);
	}

	@PutMapping("approve/{id}")
	public Loan approve(@PathVariable Integer id,
	                    @RequestParam LoanReleaseType releaseType,
	                    @RequestParam(required = false) String accountNumber,
	                    @RequestHeader("Authorization") String token) {
		return modifyResource(loanService.approve(id, releaseType, accountNumber, token));
	}

	@PutMapping("reject/{id}")
	public Loan reject(@PathVariable Integer id) {
		return modifyResource(loanService.reject(id));
	}


	@PostMapping
	@PutMapping
	public Loan persist(@RequestBody Loan loan) {
		return modifyResource(loanService.persist(loan));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		loanService.delete(id);
		return ResponseEntity.ok("Successfully deleted loan with having id: " + id);
	}

	@PutMapping("search")
	public List<Loan> search(@RequestBody Loan loan) {
		return modifyResources(loanService.search(loan));
	}

	@Override
	public Loan modifyResource(Loan loan) {
		if (loan.getAccount() != null) {
			loan.getAccount().setLoan(null);
			loan.getAccount().setSavings(null);
			loan.getAccount().setTeam(null);
			loan.getAccount().setSubAccountType(null);
			loan.getAccount().setAccountType(null);
			loan.getAccount().setShareHolder(null);
		}
		if (loan.getMember() != null) {
			loan.getMember().setLoanList(null);
			loan.getMember().setSavingsList(null);
			loan.getMember().setSubsidy(null);
			loan.getMember().setBoardMember(null);
			loan.getMember().setTeam(null);
			loan.getMember().setShareAccount(null);
		}
		return loan;
	}

	@Override
	public List<Loan> modifyResources(List<Loan> loans) {
		return loans.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
