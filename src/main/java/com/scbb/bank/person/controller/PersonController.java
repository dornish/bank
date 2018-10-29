package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.enums.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("persons")
public class PersonController {

	@GetMapping("designations")
	public Designation[] getDesignations() {
		return Designation.values();
	}

	@GetMapping("boardDesignations")
	public BoardDesignation[] getBoardDesignations() {
		return BoardDesignation.values();
	}

	@GetMapping("genders")
	public Gender[] getGenders() {
		return Gender.values();
	}

	@GetMapping("incomeTypes")
	public IncomeType[] getIncomeTypes() {
		return IncomeType.values();
	}

	@GetMapping("subsidyTypes")
	public SubsidyType[] getSubsidyTypes() {
		return SubsidyType.values();
	}

	@GetMapping("currentStatuses")
	public CurrentStatus[] getCurrentStatuses() {
		return CurrentStatus.values();
	}


}
