package com.scbb.bank.meeting.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.meeting.model.Attendance;
import com.scbb.bank.meeting.service.AttendanceService;
import com.scbb.bank.person.model.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("attendances")
public class AttendanceController implements AbstractController<Attendance, Integer> {

	private AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@GetMapping
	public List<Attendance> findAll() {
		return modifyResources(attendanceService.findAll());
	}

	@GetMapping("meeting/{id}")
	public List<Attendance> findAllByMeetingId(@PathVariable Integer id) {
		return modifyResources(attendanceService.findAllByMeetingId(id));
	}

	@GetMapping("{id}")
	public Attendance findById(@PathVariable Integer id) {
		return modifyResource(attendanceService.findById(id));
	}

	@PutMapping
	@PostMapping
	public Attendance persist(@RequestBody Attendance attendance) {
		return modifyResource(attendanceService.persist(attendance));
	}

	@PostMapping("all")
	@PutMapping("all")
	public List<Attendance> persistAll(@RequestBody List<Attendance> attendanceList) {
		return modifyResources(attendanceService.persistAll(attendanceList));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		attendanceService.delete(id);
		return ResponseEntity.ok("Successfully deleted Resource with having id: " + id);
	}

	@PutMapping("search")
	public List<Attendance> search(@RequestBody Attendance attendance) {
		return modifyResources(attendanceService.search(attendance));
	}

	public Attendance modifyResource(Attendance attendance) {
		if (attendance.getBoardMember() != null) {

			String fullName = attendance.getBoardMember().getMember().getFullName();
			attendance.getBoardMember().setMember(null);
			attendance.getBoardMember().setMember(new Member(fullName));

			attendance.getBoardMember().setUser(null);
			attendance.getBoardMember().setDivision(null);
			attendance.getBoardMember().setAttendanceList(null);
		}
		if (attendance.getMeeting() != null) {
			attendance.getMeeting().setAttendanceList(null);
		}
		return attendance;
	}

	public List<Attendance> modifyResources(List<Attendance> attendanceList) {
		return attendanceList
				.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
