package com.scbb.bank.person.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.person.model.Staff;
import com.scbb.bank.person.repository.StaffRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffService implements AbstractService<Staff, Integer> {

	private StaffRepository staffRepository;

	public StaffService(StaffRepository staffRepository) {
		this.staffRepository = staffRepository;
	}

	@Transactional
	public List<Staff> findAll() {
		return staffRepository.findAll();
	}

	@Transactional
	public List<Staff> findAllByDivisionIsNull() {
		return staffRepository.findAllByDivisionIsNull();
	}

	@Transactional
	public Staff findById(Integer id) {
		return staffRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Staff having id " + id + " cannot find"));
	}

	@Transactional
	public Staff persist(Staff staff) {
		return staffRepository.save(staff);
	}

	@Transactional
	public void delete(Integer id) {
		Staff staff = staffRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Staff having id " + id + " cannot find"));

		if (staff.getUser() != null)
			staff.getUser().setStaff(null);
		if (staff.getDivision() != null)
			staff.getDivision().setStaff(null);
		staffRepository.delete(staff);
	}

	@Transactional
	public List<Staff> search(Staff staff) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Staff> example = Example.of(staff, matcher);
		return staffRepository.findAll(example);
	}

}