package com.scbb.bank.area.service;

import com.scbb.bank.area.model.Division;
import com.scbb.bank.area.repository.DivisionRepository;
import com.scbb.bank.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DivisionService implements AbstractService<Division, Integer> {

    private DivisionRepository divisionRepository;

    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    @Override
    public List<Division> findAll() {
        return divisionRepository.findAll();
    }

    @Override
    public Division findById(Integer id) {
        return divisionRepository.getOne(id);
    }

    @Transactional
    @Override
    public Division persist(Division division) {
        return divisionRepository.save(division);
    }

    @Transactional
    @Override
    public boolean delete(Integer id) {
        Division division = divisionRepository.getOne(id);
        if (!division.getSocietyList().isEmpty())
            division.getSocietyList().forEach(society -> society.setDivision(null));
        divisionRepository.delete(division);
        return false;
    }

    @Transactional
    public List<Division> search(Division division) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Division> example = Example.of(division, matcher);
        return divisionRepository.findAll(example);
    }
}
