package com.scbb.bank.savings.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.savings.model.SavingType;
import com.scbb.bank.savings.repository.SavingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavingTypeService implements AbstractService<SavingType, Integer> {

	private SavingTypeRepository savingTypeRepository;

	public SavingTypeService(SavingTypeRepository savingTypeRepository) {
		this.savingTypeRepository = savingTypeRepository;
	}

	@Transactional
	public List<SavingType> findAll() {
		return savingTypeRepository.findAll();
	}

	@Transactional
	public SavingType findById(Integer id) {
		return savingTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
	}

	@Transactional
	public SavingType persist(SavingType savingType) {
		return savingTypeRepository.save(savingType);
	}

	@Transactional
	public void delete(Integer id) {
		savingTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
		savingTypeRepository.deleteById(id);
	}

	@Transactional
	public List<SavingType> search(SavingType savingType) {
		return null;
	}
}
