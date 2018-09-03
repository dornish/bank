package com.scbb.bank.savings.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.savings.model.Savings;
import com.scbb.bank.savings.repository.SavingsRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavingsService implements AbstractService<Savings, Integer> {

    private SavingsRepository savingsRepository;

    public SavingsService(SavingsRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    @Transactional
    public List<Savings> findAll() {
        return savingsRepository.findAll();
    }

    @Transactional
    public Savings findById(Integer id) {
        return savingsRepository.getOne(id);
    }

    @Transactional
    public Savings persist(Savings savings) {
        return savingsRepository.save(savings);
    }

    @Transactional
    public boolean delete(Integer id) {
        savingsRepository.deleteById(id);
        return true;
    }

    @Transactional
    public List<Savings> search(Savings savings) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Savings> example = Example.of(savings, matcher);

        return savingsRepository.findAll(example);
    }
}
