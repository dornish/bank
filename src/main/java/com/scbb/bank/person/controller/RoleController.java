package com.scbb.bank.person.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.person.model.Role;
import com.scbb.bank.person.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController implements AbstractController<Role, Integer> {

	private RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping
	public List<Role> findAll() {
		return modifyResources(roleService.findAll());
	}

	@GetMapping("{id}")
	public Role findById(@PathVariable Integer id) {
		return modifyResource(roleService.findById(id));
	}

	@PostMapping
	@PutMapping
	public Role persist(@RequestBody Role staff) {
		return modifyResource(roleService.persist(staff));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		roleService.delete(id);
		return ResponseEntity.ok("Successfully deleted role with having id: " + id);
	}

	@Override
	public List<Role> search(Role role) {
		return null;
	}

	public Role modifyResource(Role staff) {
		return staff;
	}

	public List<Role> modifyResources(List<Role> staffList) {
		return staffList;
	}
}
