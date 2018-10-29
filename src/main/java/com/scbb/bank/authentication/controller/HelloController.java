package com.scbb.bank.authentication.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/hello")
public class HelloController {

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping(/*produces="application/json; charset=UTF-8"*/)
	public String hello() {
		return "Hello World !!!";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/secured"/*, produces="application/json; charset=UTF-8"*/)
	public String helloWithAdmin() {
		return "Hello World From Admin !!!";
	}
}
