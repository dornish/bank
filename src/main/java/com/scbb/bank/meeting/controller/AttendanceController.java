package com.scbb.bank.meeting.controller;

import com.scbb.bank.meeting.model.Attendance;
import com.scbb.bank.meeting.service.AttendanceService;
import com.scbb.bank.person.controller.AbstractController;
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
    public ResponseEntity<List<Attendance>> findAll() {
        return ResponseEntity.ok(modifyResources(attendanceService.findAll()));
    }

    @GetMapping("meeting/{id}")
    public ResponseEntity<List<Attendance>> findByMeetingId(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResources(attendanceService.findByMeetingId(id)));
    }

    @GetMapping("{id}")
    public ResponseEntity<Attendance> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(attendanceService.findById(id)));
    }

    @PutMapping
    @PostMapping
    public ResponseEntity<Attendance> persist(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(modifyResource(attendanceService.persist(attendance)));
    }

    @PostMapping("all")
    @PutMapping("all")
    public ResponseEntity<List<Attendance>> persistAll(@RequestBody List<Attendance> attendanceList) {
        return ResponseEntity.ok(modifyResources(attendanceService.persistAll(attendanceList)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        attendanceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("search")
    public ResponseEntity<List<Attendance>> search(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(modifyResources(attendanceService.search(attendance)));
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
