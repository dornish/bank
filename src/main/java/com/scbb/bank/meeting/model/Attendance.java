package com.scbb.bank.meeting.model;

import com.scbb.bank.meeting.model.enums.AttendanceType;
import com.scbb.bank.person.model.BoardMember;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private AttendanceType attendanceType;

	private String remarks;

	@ManyToOne
	private Meeting meeting;

	@ManyToOne
	private BoardMember boardMember;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Attendance)) return false;
		Attendance that = (Attendance) o;
		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
