package com.scbb.bank.area.service;

import com.scbb.bank.area.model.Team;
import com.scbb.bank.area.repository.TeamRepository;
import com.scbb.bank.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService implements AbstractService<Team, Integer> {


    private TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Transactional
    public Team findById(Integer id) {
        return teamRepository.getOne(id);
    }

    @Transactional
    public Team persist(Team team) {
        return teamRepository.save(team);
    }

    @Transactional
    public boolean delete(Integer id) {
        Team team = teamRepository.getOne(id);
        if (!team.getMemberList().isEmpty())
            team.getMemberList().forEach(member -> member.setTeam(null));
        teamRepository.delete(team);
        return false;
    }


    @Transactional
    public List<Team> search(Team team) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Team> example = Example.of(team, matcher);
        return teamRepository.findAll(example);
    }

    public List<Team> findAllBySocietyId(Integer id) {
        return teamRepository.findAllBySocietyId(id);
    }
}
