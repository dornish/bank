package com.scbb.bank.person.controller;

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
    public ResponseEntity<List<Role>> findAll() {
        return ResponseEntity.ok(modifyResources(roleService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(roleService.findById(id)));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Role> persist(@RequestBody Role staff) {
        return ResponseEntity.ok(modifyResource(roleService.persist(staff)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.ok(true);
    }

    @Override
    public ResponseEntity<List<Role>> search(Role role) {
        return null;
    }

    public Role modifyResource(Role staff) {
        return staff;
    }

    public List<Role> modifyResources(List<Role> staffList) {
        return staffList;
    }
}
