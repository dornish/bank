package com.scbb.bank.person.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "division")
    private Set<Society> societyList = new HashSet<>();

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
