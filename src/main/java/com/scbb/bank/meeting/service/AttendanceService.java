package com.scbb.bank.meeting.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.meeting.model.Attendance;
import com.scbb.bank.meeting.repository.AttendanceRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService implements AbstractService<Attendance, Integer> {

	private AttendanceRepository attendanceRepository;

	public AttendanceService(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
	}

	@Transactional
	public List<Attendance> findAll() {
		return attendanceRepository.findAll();
	}

	@Transactional
	public Attendance findById(Integer id) {
		return attendanceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
	}

	@Transactional
	public List<Attendance> findAllByMeetingId(Integer id) {
		return attendanceRepository.findAllByMeetingId(id);
	}

	@Transactional
	public Attendance persist(Attendance attendance) {
		return attendanceRepository.save(attendance);
	}

	@Transactional
	public List<Attendance> persistAll(List<Attendance> attendanceList) {
		return attendanceList
				.stream()
				.map(this::persist)
				.collect(Collectors.toList());
	}

	@Transactional
	public void delete(Integer id) {
		attendanceRepository.delete(attendanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attendance having id " + id + " cannot find")));
	}

	@Transactional
	public List<Attendance> search(Attendance attendance) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Attendance> example = Example.of(attendance, matcher);
		return attendanceRepository.findAll(example);
	}


}
