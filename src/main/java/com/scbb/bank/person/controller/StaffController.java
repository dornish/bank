package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.Staff;
import com.scbb.bank.person.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/staff")
public class StaffController implements AbstractController<Staff, Integer> {

    private StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<Staff>> findAll() {
        return ResponseEntity.ok(modifyResources(staffService.findAll()));
    }

    @GetMapping("/divisionIsNull")
    public ResponseEntity<List<Staff>> findAllByDivisionIsNull() {
        return ResponseEntity.ok(modifyResources(staffService.findAllByDivisionIsNull()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Staff> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(staffService.findById(id)));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Staff> persist(@RequestBody Staff staff) {
        return ResponseEntity.ok(modifyResource(staffService.persist(staff)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        staffService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/search")
    public ResponseEntity<List<Staff>> search(@RequestBody Staff staff){
        return ResponseEntity.ok(modifyResources(staffService.search(staff)));
    }


    public Staff modifyResource(Staff staff) {
        if (staff.getDivision() != null) {
            staff.getDivision().setStaff(null);
            staff.getDivision().setSocietyList(null);
            staff.getDivision().setBoardMember(null);
        }
        if (staff.getUser() != null) {
            staff.getUser().setStaff(null);
            staff.getUser().setAuthorities(null);
            staff.getUser().setBoardMember(null);
        }
        return staff;
    }

    public List<Staff> modifyResources(List<Staff> staffList) {
        return staffList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }

}
