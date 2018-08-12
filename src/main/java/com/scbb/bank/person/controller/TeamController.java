package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.Team;
import com.scbb.bank.person.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("teams")
public class TeamController implements AbstractController<Team, Integer> {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping
    public ResponseEntity<List<Team>> findAll() {
        return ResponseEntity.ok(modifyResources(teamService.findAll()));
    }

    @GetMapping("society/{id}")
    public ResponseEntity<List<Team>> findAllBySocietyId(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResources(teamService.findAllBySocietyId(id)));
    }

    @GetMapping("{id}")
    public ResponseEntity<Team> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(teamService.findById(id)));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Team> persist(@RequestBody Team team) {
        return ResponseEntity.ok(modifyResource(teamService.persist(team)));
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        teamService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("search")
    public ResponseEntity<List<Team>> search(@RequestBody Team team){
        return ResponseEntity.ok(modifyResources(teamService.search(team)));
    }

    public Team modifyResource(Team team) {
        if (team.getSociety() != null) {
            team.getSociety().setTeamList(null);
            team.getSociety().setDivision(null);
        }
        team.getMemberSet().forEach(member -> {
            member.setTeam(null);
            member.setSubsidy(null);
            member.setBoardMember(null);
        });
        return team;
    }

    public List<Team> modifyResources(List<Team> teamList) {
        return teamList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
