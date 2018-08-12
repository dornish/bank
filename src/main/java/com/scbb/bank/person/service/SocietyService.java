package com.scbb.bank.person.service;

import com.scbb.bank.person.repository.DivisionRepository;
import com.scbb.bank.person.repository.SocietyRepository;
import com.scbb.bank.person.repository.TeamRepository;
import com.scbb.bank.person.model.Division;
import com.scbb.bank.person.model.Society;
import com.scbb.bank.person.model.Team;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public Society findById(Integer id) {
        return societyRepository.getOne(id);
    }

    @Override
    public Society persist(Society society) {
        return societyRepository.save(society);
    }

    @Override
    public void delete(Integer id) {
        Society society = societyRepository.getOne(id);
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

    @Transactional
    public List<Society> findAllByDivisionId(Integer id) {
        return societyRepository.findAllByDivisionId(id);
    }
}
