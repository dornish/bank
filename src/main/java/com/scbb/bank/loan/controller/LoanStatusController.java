package com.scbb.bank.loan.controller;

import com.scbb.bank.loan.model.enums.LoanStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("loanStatuses")
public class LoanStatusController {

	@GetMapping
	public LoanStatus[] findAll() {
		return LoanStatus.values();
	}
}
