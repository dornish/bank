package com.scbb.bank.meeting.controller;

import com.scbb.bank.meeting.model.enums.MeetingStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/meetingStatuses")
public class MeetingStatusController {

    @GetMapping
    public ResponseEntity<MeetingStatus[]> findAll() {
        return ResponseEntity.ok(MeetingStatus.values());
    }

}
