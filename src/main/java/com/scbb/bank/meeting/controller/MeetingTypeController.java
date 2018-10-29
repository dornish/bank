package com.scbb.bank.meeting.controller;

import com.scbb.bank.meeting.model.enums.MeetingType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/meetingTypes")
public class MeetingTypeController {

	@GetMapping
	public ResponseEntity<MeetingType[]> findAll() {
		return ResponseEntity.ok(MeetingType.values());
	}
}
