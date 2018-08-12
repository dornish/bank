package com.scbb.bank.meeting.repository;

import com.scbb.bank.meeting.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
}
