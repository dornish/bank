package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.enums.Gender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/genders")
public class GenderController {

    @GetMapping
    public ResponseEntity<Gender[]> findAll() {
        return ResponseEntity.ok(Gender.values());
    }
}
