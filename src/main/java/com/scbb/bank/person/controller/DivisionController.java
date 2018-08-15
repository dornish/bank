package com.scbb.bank.person.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.person.model.Division;
import com.scbb.bank.person.model.Member;
import com.scbb.bank.person.service.DivisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/divisions")
public class DivisionController implements AbstractController<Division, Integer> {

    private DivisionService divisionService;

    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @GetMapping
    public List<Division> findAll() {
        return modifyResources(divisionService.findAll());
    }

    @GetMapping("{id}")
    public Division findById(@PathVariable Integer id) {
        return modifyResource(divisionService.findById(id));
    }

    @PostMapping
    @PutMapping
    public Division persist(@RequestBody Division division) {
        return modifyResource(divisionService.persist(division));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        divisionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/search")
    public List<Division> search(@RequestBody Division division) {
        return modifyResources(divisionService.search(division));
    }


    public Division  modifyResource(Division division) {
        if (!division.getSocietyList().isEmpty())
            division.getSocietyList().forEach(society -> {
                society.setDivision(null);
                society.setTeamList(null);
            });
        if (division.getBoardMember() != null) {
            division.getBoardMember().setUser(null);
            division.getBoardMember().setDivision(null);
            division.getBoardMember().setAttendanceList(null);

            String fullName = division.getBoardMember().getMember().getFullName();
            division.getBoardMember().setMember(null);
            division.getBoardMember().setMember(new Member(fullName));
        }
        if (division.getStaff() != null) {
            division.getStaff().setDivision(null);
            division.getStaff().setUser(null);
            division.getStaff().setUser(null);
        }
        return division;
    }

    public List<Division> modifyResources(List<Division> divisionList) {
        return divisionList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }

}
