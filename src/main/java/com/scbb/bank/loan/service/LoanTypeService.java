package com.scbb.bank.loan.service;


import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.loan.model.LoanType;
import com.scbb.bank.loan.repository.LoanTypeRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanTypeService implements AbstractService<LoanType, Integer> {

	private LoanTypeRepository loanTypeRepository;

	public LoanTypeService(LoanTypeRepository loanTypeRepository) {
		this.loanTypeRepository = loanTypeRepository;
	}

	@Override
	public List<LoanType> findAll() {
		return loanTypeRepository.findAll();
	}

	@Override
	public LoanType findById(Integer id) {
		return loanTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan type having id " + id + " cannot find"));
	}

	@Override
	public LoanType persist(LoanType loanType) {
		return loanTypeRepository.save(loanType);
	}

	@Override
	public void delete(Integer id) {
		loanTypeRepository.delete(
				loanTypeRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Loan type having id " + id + " cannot find"))
		);
	}

	@Override
	public List<LoanType> search(LoanType loanType) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<LoanType> example = Example.of(loanType, matcher);
		return loanTypeRepository.findAll(example);
	}
}
