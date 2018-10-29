package com.scbb.bank.person.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.person.model.Role;
import com.scbb.bank.person.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Transactional
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Transactional
	public Role findById(Integer id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role having id " + id + " cannot find"));
	}

	@Transactional
	public Role persist(Role role) {
		return roleRepository.save(role);
	}

	@Transactional
	public void delete(Integer id) {
		roleRepository.delete(
				roleRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Role having id " + id + " cannot find"))
		);

	}
}
