package com.scbb.bank.meeting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.meeting.model.enums.MeetingStatus;
import com.scbb.bank.meeting.model.enums.MeetingType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private MeetingStatus meetingStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    @OneToMany(mappedBy = "meeting", orphanRemoval = true)
    private List<Attendance> attendanceList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meeting)) return false;
        Meeting meeting = (Meeting) o;
        return id != null ? id.equals(meeting.id) : meeting.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
