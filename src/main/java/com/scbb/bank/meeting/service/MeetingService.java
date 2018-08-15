package com.scbb.bank.meeting.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.meeting.model.Meeting;
import com.scbb.bank.meeting.repository.MeetingRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingService implements AbstractService<Meeting, Integer> {

    private MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Transactional
    public List<Meeting> findAll() {
        return meetingRepository.findAll();
    }

    @Transactional
    public Meeting findById(Integer id) {
        return meetingRepository.getOne(id);
    }

    @Transactional
    public Meeting persist(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @Transactional
    public boolean delete(Integer id) {
        Meeting meeting = meetingRepository.getOne(id);
        meeting.getAttendanceList().forEach(attendance -> {
            attendance.setMeeting(null);
            attendance.setBoardMember(null);
        });
        meetingRepository.delete(meeting);
        return false;
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
}
