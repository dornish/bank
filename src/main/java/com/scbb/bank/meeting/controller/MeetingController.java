package com.scbb.bank.meeting.controller;

import com.scbb.bank.meeting.model.Meeting;
import com.scbb.bank.meeting.service.MeetingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/meetings")
public class MeetingController {

	private MeetingService meetingService;

	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;
	}

	@GetMapping
	public List<Meeting> findAll() {
		return modifyResources(meetingService.findAll());
	}

	@GetMapping("{id}")
	public Meeting findById(@PathVariable Integer id) {
		return modifyResource(meetingService.findById(id));
	}

	@GetMapping("sms/{id}")
	public void sms(@PathVariable Integer id) {
		meetingService.sms(id);
	}

	@PostMapping
	@PutMapping
	public Meeting persist(@RequestBody Meeting meeting) {
		return modifyResource(meetingService.persist(meeting));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		meetingService.delete(id);
		return ResponseEntity.ok("Successfully deleted Resource with having id: " + id);
	}

	@PutMapping("search")
	public List<Meeting> search(
			@RequestBody Meeting meeting,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate fromDate,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate toDate) {
		return modifyResources(meetingService.search(meeting, fromDate, toDate));
	}


	public Meeting modifyResource(Meeting meeting) {
		if (meeting.getAttendanceList() != null)
			meeting.getAttendanceList()
					.forEach(attendance -> {
						attendance.setMeeting(null);
						attendance.setBoardMember(null);
					});
		return meeting;
	}


	public List<Meeting> modifyResources(List<Meeting> meetingList) {
		return meetingList
				.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
