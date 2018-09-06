package com.scbb.bank.savings.controller;

import com.scbb.bank.savings.model.enums.SavingStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("savingStatus")
public class SavingStatusController {

    @GetMapping
    public SavingStatus[] findAll() {
        return SavingStatus.values();
    }
}
