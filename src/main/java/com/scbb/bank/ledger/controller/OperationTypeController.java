package com.scbb.bank.ledger.controller;

import com.scbb.bank.ledger.model.enums.OperationType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("operationTypes")
public class OperationTypeController {

    @GetMapping
    public OperationType[] findAll() {
        return OperationType.values();
    }
}
