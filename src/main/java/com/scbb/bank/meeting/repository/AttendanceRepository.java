package com.scbb.bank.meeting.repository;

import com.scbb.bank.meeting.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findAllByMeetingId(Integer id);
}
