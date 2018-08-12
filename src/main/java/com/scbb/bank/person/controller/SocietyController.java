package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.Society;
import com.scbb.bank.person.service.SocietyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/societies")
public class SocietyController implements AbstractController<Society, Integer> {

    private SocietyService societyService;

    public SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @GetMapping
    public ResponseEntity<List<Society>> findAll() {
        return ResponseEntity.ok(modifyResources(societyService.findAll()));
    }

    @GetMapping("division/{id}")
    public ResponseEntity<List<Society>> findAllByDivisionId(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResources(societyService.findAllByDivisionId(id)));
    }

    @GetMapping("{id}")
    public ResponseEntity<Society> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(societyService.findById(id)));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Society> persist(@RequestBody Society society) {
        return ResponseEntity.ok(modifyResource(societyService.persist(society)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        societyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/search")
    public ResponseEntity<List<Society>> search(@RequestBody Society society){
        return ResponseEntity.ok(modifyResources(societyService.search(society)));
    }

    public Society modifyResource(Society society) {
        if (society.getTeamList() != null) {
            society.getTeamList().forEach(team -> {
                team.setSociety(null);
                team.setMemberSet(null);
            });
        }
        if (society.getDivision() != null) {
            society.getDivision().setBoardMember(null);
            society.getDivision().setStaff(null);
            society.getDivision().setSocietyList(null);
        }
        return society;
    }

    public List<Society> modifyResources(List<Society> societyList) {
        return societyList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
