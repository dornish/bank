package com.scbb.bank.meeting.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.meeting.model.Meeting;
import com.scbb.bank.meeting.model.enums.MeetingStatus;
import com.scbb.bank.meeting.repository.MeetingRepository;
import com.scbb.bank.person.model.enums.CurrentStatus;
import com.scbb.bank.person.service.BoardMemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService implements AbstractService<Meeting, Integer> {

	private MeetingRepository meetingRepository;
	private BoardMemberService boardMemberService;
	private RestTemplate restTemplate;

	@Value("${sms.api.id}")
	private String id;

	@Value("${sms.api.pw}")
	private String pw;

	public MeetingService(MeetingRepository meetingRepository, BoardMemberService boardMemberService, RestTemplate restTemplate) {
		this.meetingRepository = meetingRepository;
		this.boardMemberService = boardMemberService;
		this.restTemplate = restTemplate;
	}

	@Transactional
	public List<Meeting> findAll() {
		return meetingRepository.findAll();
	}

	@Transactional
	public Meeting findById(Integer id) {
		return meetingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
	}

	@Transactional
	public Meeting persist(Meeting meeting) {
		return meetingRepository.save(meeting);
	}

	@Transactional
	public void delete(Integer id) {
		Meeting meeting = meetingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));

		meeting.getAttendanceList().forEach(attendance -> {
			attendance.setMeeting(null);
			attendance.setBoardMember(null);
		});
		meetingRepository.deleteById(id);
	}

	@Transactional
	public List<Meeting> search(Meeting meeting) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Meeting> example = Example.of(meeting, matcher);
		return meetingRepository.findAll(example);
	}

	@Transactional
	public void sms(Integer id) {
		Meeting meeting = findById(id);
		String baseUrl = "http://www.textit.biz/sendmsg/index.php?";
		String numbers = boardMemberService.findAll().stream()
				.filter(boardMember -> boardMember.getCurrentStatus() == CurrentStatus.Active)
				.map(boardMember -> boardMember.getMember().getTelephone() != null ? boardMember.getMember().getTelephone().substring(1) : "")
				.collect(Collectors.joining(","));
		numbers = numbers.substring(0, numbers.length() - 1);
		String msg;
		if (meeting.getMeetingStatus() == MeetingStatus.To_Be_Held)
			msg = "Next+Control+Board+meeting+will+be+held+on+"
					+ meeting.getDate() + "+at+" + meeting.getTime().format(DateTimeFormatter.ofPattern("h:mma"));
		else
			msg = "Meeting+supposed+to+hold+on+"
					+ meeting.getDate() + "+at+" + meeting.getTime().format(DateTimeFormatter.ofPattern("h:mma"))
					+ "+has+been+canceled";
		String fullUrl = baseUrl +
				"id=" + this.id + "&" +
				"password=" + this.pw + "&" +
				"text=" + msg + "&" +
				"to=" + numbers + "&" +
				"eco=" + "Y";
		String response = restTemplate.getForObject(URI.create(fullUrl), String.class);
		System.out.println(response);

	}
}
