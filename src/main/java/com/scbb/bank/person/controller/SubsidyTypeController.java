package com.scbb.bank.person.controller;


import com.scbb.bank.person.model.enums.SubsidyType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("subsidyTypes")
public class SubsidyTypeController {

    @GetMapping
    public ResponseEntity<SubsidyType[]> findAll() {
        return ResponseEntity.ok(SubsidyType.values());
    }
}
