package com.scbb.bank.meeting.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.meeting.model.Meeting;
import com.scbb.bank.meeting.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/meetings")
public class MeetingController implements AbstractController<Meeting, Integer> {

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
	public List<Meeting> search(@RequestBody Meeting meeting) {
		return modifyResources(meetingService.search(meeting));
	}

	@Override
	public Meeting modifyResource(Meeting meeting) {
		if (meeting.getAttendanceList() != null)
			meeting.getAttendanceList()
					.forEach(attendance -> {
						attendance.setMeeting(null);
						attendance.setBoardMember(null);
					});
		return meeting;
	}

	@Override
	public List<Meeting> modifyResources(List<Meeting> meetingList) {
		return meetingList
				.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
