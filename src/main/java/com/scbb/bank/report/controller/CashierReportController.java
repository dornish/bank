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
}
