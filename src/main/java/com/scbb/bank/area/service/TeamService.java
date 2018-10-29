package com.scbb.bank.area.service;

import com.scbb.bank.area.model.Team;
import com.scbb.bank.area.repository.TeamRepository;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.service.AccountService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService implements AbstractService<Team, Integer> {


	private TeamRepository teamRepository;
	private AccountService accountService;

	public TeamService(TeamRepository teamRepository, AccountService accountService) {
		this.teamRepository = teamRepository;
		this.accountService = accountService;
	}

	@Transactional
	public List<Team> findAll() {
		teamRepository.report().forEach(report -> System.out.println("team is " + report.getTeam() + " and its count is " + report.getCount()));
		return teamRepository.findAll();
	}

	@Transactional
	public Team findById(Integer id) {
		return teamRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Team having id" + id + " cannot find"));
	}

	@Transactional
	public Team persist(Team team) {
		if (team.getId() == null)
			accountService.persist(team.getAccount());
		return teamRepository.save(team);
	}

	@Transactional
	public void delete(Integer id) {
		Team team = teamRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Team having id" + id + " cannot find"));
		if (!team.getMemberList().isEmpty())
			team.getMemberList().forEach(member -> member.setTeam(null));
		teamRepository.delete(team);
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
