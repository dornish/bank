package com.scbb.bank.loan.service;


import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.loan.model.LoanType;
import com.scbb.bank.loan.repository.LoanTypeRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoanTypeService {

	private LoanTypeRepository loanTypeRepository;

	public LoanTypeService(LoanTypeRepository loanTypeRepository) {
		this.loanTypeRepository = loanTypeRepository;
	}

	@Transactional
	public List<LoanType> findAll() {
		return loanTypeRepository.findAll();
	}

	@Transactional
	public LoanType findById(Integer id) {
		return loanTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan type having id " + id + " cannot find"));
	}

	@Transactional
	public LoanType persist(LoanType loanType) {
		return loanTypeRepository.save(loanType);
	}

	@Transactional
	public void delete(Integer id) {
		loanTypeRepository.delete(
				loanTypeRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Loan type having id " + id + " cannot find"))
		);
	}

	public List<LoanType> search(LoanType loanType, BigDecimal amount, Integer period) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<LoanType> example = Example.of(loanType, matcher);
		List<LoanType> loanTypeList = loanTypeRepository.findAll(example);
		return loanTypeList.stream()
				.filter(loanType1 -> amount == null || loanType1.getMinAmount().compareTo(amount) <= 0)
				.filter(loanType1 -> amount == null || loanType1.getMaxAmount().compareTo(amount) >= 0)
				.filter(loanType1 -> period == null || loanType1.getMinPeriod().compareTo(period) <= 0)
				.filter(loanType1 -> period == null || loanType1.getMaxPeriod().compareTo(period) >= 0)
				.collect(Collectors.toList());
	}
}
