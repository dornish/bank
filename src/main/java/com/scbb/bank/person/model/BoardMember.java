package com.scbb.bank.person.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.area.model.Division;
import com.scbb.bank.meeting.model.Attendance;
import com.scbb.bank.person.model.enums.BoardDesignation;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointedDate;

    @Enumerated(EnumType.STRING)
    private BoardDesignation boardDesignation;

    @OneToOne
    private Member member;

    @OneToOne(mappedBy = "boardMember")
    private Division division;

    @OneToOne(mappedBy = "boardMember", cascade = CascadeType.ALL)
    private User user;


    @OneToMany(mappedBy = "boardMember")
    private Set<Attendance> attendanceList = new HashSet<>();

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
