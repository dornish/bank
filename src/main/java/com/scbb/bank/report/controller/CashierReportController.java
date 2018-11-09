package com.scbb.bank.report.controller;


import com.scbb.bank.report.service.CashierReportService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("cashier")
public class CashierReportController {

	private CashierReportService cashierReportService;

	public CashierReportController(CashierReportService cashierReportService) {
		this.cashierReportService = cashierReportService;
	}


	@GetMapping("deposit/transaction/{id}")
	public void deposit(@PathVariable Integer id,
	                    @RequestHeader("Authorization") String token,
	                    @RequestParam(required = false) String operationType,
	                    HttpServletResponse response) {
		cashierReportService.deposit(id, token, operationType, response);
	}

	@GetMapping("shares/transaction/{id}")
	public void share(@PathVariable Integer id,
	                  @RequestHeader("Authorization") String token,
	                  HttpServletResponse response) {
		cashierReportService.share(id, token, response);
	}

	@GetMapping("trams/transaction/{id}")
	public void team(@PathVariable Integer id,
	                 @RequestHeader("Authorization") String token,
	                 HttpServletResponse response) {
		cashierReportService.team(id, token, response);
	}

	@GetMapping("loans/transaction/{id}")
	public void loan(@PathVariable Integer id,
	                 @RequestHeader("Authorization") String token,
	                 HttpServletResponse response) {
		cashierReportService.loan(id, token, response);
	}
}

