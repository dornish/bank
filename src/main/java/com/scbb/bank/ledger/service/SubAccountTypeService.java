package com.scbb.bank.ledger.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.SubAccountType;
import com.scbb.bank.ledger.repository.SubAccountTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubAccountTypeService implements AbstractService<SubAccountType, Integer> {

	private SubAccountTypeRepository subAccountTypeRepository;

	public SubAccountTypeService(SubAccountTypeRepository subAccountTypeRepository) {
		this.subAccountTypeRepository = subAccountTypeRepository;
	}

	@Transactional
	public List<SubAccountType> findAll() {
		return subAccountTypeRepository.findAll();
	}

	public List<SubAccountType> findAllByAccountTypeId(Integer id) {
		return subAccountTypeRepository.findAllByAccountTypeId(id);
	}

	@Transactional
	public SubAccountType findById(Integer id) {
		return subAccountTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sub account type having id" + id + " cannot find"));
	}

	@Override
	public SubAccountType persist(SubAccountType subAccountType) {
		return null;
	}

	@Override
	public void delete(Integer id) {
	}

	@Override
	public List<SubAccountType> search(SubAccountType subAccountType) {
		return null;
	}
}
