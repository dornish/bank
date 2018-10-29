package com.scbb.bank.meeting.controller;

import com.scbb.bank.meeting.model.enums.AttendanceType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/attendanceTypes")
public class AttendanceTypeController {

	@GetMapping
	public ResponseEntity<AttendanceType[]> findAll() {
		return ResponseEntity.ok(AttendanceType.values());
	}
}
