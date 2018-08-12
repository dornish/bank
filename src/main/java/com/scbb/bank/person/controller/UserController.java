package com.scbb.bank.person.controller;

import com.scbb.bank.person.model.User;
import com.scbb.bank.person.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController implements AbstractController<User, Integer> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(modifyResources(userService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(userService.findById(id)));
    }

    @GetMapping("/staff")
    public ResponseEntity<List<User>> findAllByHavingStaff() {
        return ResponseEntity.ok(modifyResources(userService.findAllByHavingStaff()));
    }

    @GetMapping("/boardMember")
    public ResponseEntity<List<User>> findAllByHavingBoardMember() {
        return ResponseEntity.ok(modifyResources(userService.findAllByHavingBoardMember()));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<User> persist(@RequestBody User user) {
        return ResponseEntity.ok(modifyResource(userService.persist(user)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/search")
    public ResponseEntity<List<User>> search(@RequestBody User user) {
        return ResponseEntity.ok(modifyResources(userService.search(user)));
    }

    public User modifyResource(User user) {
        if (user.getStaff() != null) {
            user.getStaff().setUser(null);
            user.getStaff().setDivision(null);
        }
        if (user.getBoardMember() != null) {
            user.getBoardMember().setUser(null);
            user.getBoardMember().setDivision(null);
            user.getBoardMember().setMember(null);
            user.getBoardMember().setAttendanceList(null);
        }
        return user;
    }

    public List<User> modifyResources(List<User> userList) {
        return userList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }

}
