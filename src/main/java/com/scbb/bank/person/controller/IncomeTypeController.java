package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.enums.IncomeType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("incomeTypes")
public class IncomeTypeController {

    @GetMapping
    public ResponseEntity<IncomeType[]> findAll() {
        return ResponseEntity.ok(IncomeType.values());
    }
}
