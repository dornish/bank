package com.scbb.bank.person.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.area.model.Division;
import com.scbb.bank.meeting.model.Attendance;
import com.scbb.bank.person.model.enums.BoardDesignation;
import com.scbb.bank.person.model.enums.CurrentStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class BoardMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointedDate;

	@Enumerated(EnumType.STRING)
	private BoardDesignation boardDesignation;

	@Enumerated(EnumType.STRING)
	private CurrentStatus currentStatus;

	@OneToOne
	private Member member;

	@OneToOne(mappedBy = "boardMember")
	private Division division;

	@OneToOne(mappedBy = "boardMember")
	private User user;

	@OneToMany(mappedBy = "boardMember")
	private List<Attendance> attendanceList = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BoardMember)) return false;
		BoardMember that = (BoardMember) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
