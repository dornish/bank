package com.scbb.bank.other;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.person.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;


	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return userRepository.findByUsername(s)
				.orElseThrow(() -> new UsernameNotFoundException(s + " : user can't find"));
	}

	@Transactional
	public UserDetails loadUserById(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User having id " + id + " cannot find"));
	}

	@Transactional
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public User findById(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User having id " + id + " cannot find"));
	}

	@Transactional
	public List<User> findAllByHavingStaff() {
		return userRepository.findAllByStaffIsNotNull();
	}

	@Transactional
	public List<User> findAllByHavingBoardMember() {
		return userRepository.findAllByBoardMemberIsNotNull();
	}

	@Transactional
	public User persist(User user) {
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		else
			user.setPassword(userRepository.getOne(user.getId()).getPassword());
		return userRepository.save(user);
	}

	@Transactional
	public void delete(Integer id) {
		userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User having id " + id + " cannot find")));
	}

	@Transactional
	public List<User> search(User user) {
		user.setEnabled(true);
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<User> userExample = Example.of(user, matcher);
		return userRepository.findAll(userExample);
	}

}
