package com.scbb.bank.loan.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.payload.InstallmentScheduleRequest;
import com.scbb.bank.loan.payload.InstallmentScheduleResponse;
import com.scbb.bank.loan.payload.LoanStatusRequest;
import com.scbb.bank.loan.payload.LoanStatusResponse;
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

    @GetMapping("{id}")
    public Loan findById(@PathVariable Integer id) {
        return modifyResource(loanService.findById(id));
    }

    @GetMapping("calcAmountToBePaid/{id}")
    public BigDecimal calcAmountToBePaid(@PathVariable Integer id) {
        return loanService.remainingInstallmentAmount(id);
    }

    @PutMapping("calcInterestAndFine")
    public LoanStatusResponse calculateInterestAndFine(@RequestBody LoanStatusRequest loanStatusRequest) {
        return loanService.calculateInterest(loanStatusRequest);
    }

    @PutMapping("calcSchedule")
    public List<InstallmentScheduleResponse> calculateInstallmentSchedule(@RequestBody InstallmentScheduleRequest request) {
        return loanService.calculateInstallmentSchedule(request);
    }

    @PutMapping("pay/{id}")
    public Loan payInstallment(@PathVariable Integer id) {
        return modifyResource(loanService.payInstallment(id));
    }

    @PutMapping("/approve/{id}")
    public Loan approve(@PathVariable Integer id) {
        return modifyResource(loanService.approve(id));
    }

    @PostMapping
    @PutMapping
    public Loan persist(@RequestBody Loan loan) {
        return modifyResource(loanService.persist(loan));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        if (loanService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public List<Loan> search(Loan loan) {
        return null;
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
