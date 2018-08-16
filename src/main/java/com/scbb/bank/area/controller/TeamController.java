package com.scbb.bank.area.controller;

import com.scbb.bank.area.model.Team;
import com.scbb.bank.area.service.TeamService;
import com.scbb.bank.interfaces.AbstractController;
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
    public List<Team> findAll() {
        return modifyResources(teamService.findAll());
    }

    @GetMapping("society/{id}")
    public List<Team> findAllBySocietyId(@PathVariable Integer id) {
        return modifyResources(teamService.findAllBySocietyId(id));
    }

    @GetMapping("{id}")
    public Team findById(@PathVariable Integer id) {
        return modifyResource(teamService.findById(id));
    }

    @PostMapping
    @PutMapping
    public Team persist(@RequestBody Team team) {
        return modifyResource(teamService.persist(team));
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        teamService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("search")
    public List<Team> search(@RequestBody Team team) {
        return modifyResources(teamService.search(team));
    }

    public Team modifyResource(Team team) {
        if (team.getSociety() != null) {
            team.getSociety().setTeamList(null);
            team.getSociety().setDivision(null);
        }
        team.getMemberList().forEach(member -> {
            member.setTeam(null);
            member.setSubsidy(null);
            member.setBoardMember(null);
        });
        if (team.getAccount() != null) {
            team.getAccount().setSubAccountType(null);
            team.getAccount().setTeam(null);
            team.getAccount().setAccountType(null);
        }
        return team;
    }

    public List<Team> modifyResources(List<Team> teamList) {
        return teamList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
