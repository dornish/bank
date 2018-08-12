package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.enums.Designation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/designations")
public class DesignationController {

    @GetMapping
    public ResponseEntity<Designation[]> findAll() {
        return ResponseEntity.ok(Designation.values());
    }

}
