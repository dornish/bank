package com.scbb.bank.area.model;

import com.scbb.bank.person.model.BoardMember;
import com.scbb.bank.person.model.Staff;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "division")
    private List<Society> societyList = new ArrayList<>();

    @OneToOne
    private Staff staff;

    @OneToOne
    private BoardMember boardMember;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Division)) return false;

        Division division = (Division) o;

        return id != null ? id.equals(division.id) : division.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
