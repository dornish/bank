package com.scbb.bank.ledger.controller;

import com.scbb.bank.ledger.model.enums.EntryType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("entryTypes")
public class EntryTypeController {

	@GetMapping
	public EntryType[] findAll() {
		return EntryType.values();
	}
}
