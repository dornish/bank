package com.scbb.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<String> resourceNotFound(ResourceNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<String> usernameNotFound(UsernameNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<String> usernameNotFound(ResourceCannotDeleteException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<String> internalServerError(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

}
