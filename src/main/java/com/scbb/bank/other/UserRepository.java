package com.scbb.bank.other;


import com.scbb.bank.person.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	List<User> findAllByStaffIsNotNull();

	List<User> findAllByBoardMemberIsNotNull();

}
