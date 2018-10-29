package com.scbb.bank.area.service;

import com.scbb.bank.area.model.Society;
import com.scbb.bank.area.repository.SocietyRepository;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SocietyService implements AbstractService<Society, Integer> {

	private SocietyRepository societyRepository;

	public SocietyService(SocietyRepository societyRepository) {
		this.societyRepository = societyRepository;
	}

	@Override
	public List<Society> findAll() {
		return societyRepository.findAll();
	}

	@Transactional
	public List<Society> findAllByDivisionId(Integer id) {
		return societyRepository.findAllByDivisionId(id);
	}

	@Override
	public Society findById(Integer id) {
		return societyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Society having id" + id + " cannot find"));
	}

	@Override
	public Society persist(Society society) {
		return societyRepository.save(society);
	}

	@Override
	public void delete(Integer id) {
		Society society = societyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Society having id" + id + " cannot find"));
		if (!society.getTeamList().isEmpty())
			society.getTeamList().forEach(team -> team.setSociety(null));
		societyRepository.delete(society);
	}

	@Transactional
	public List<Society> search(Society society) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Society> example = Example.of(society, matcher);
		return societyRepository.findAll(example);
	}
}
