package com.scbb.bank.person.model;

import com.scbb.bank.person.model.enums.SubsidyType;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class Subsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SubsidyType subsidyType;

    private Integer amount;

    private String number;

    @OneToOne
    private Member member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subsidy)) return false;
        Subsidy subsidy = (Subsidy) o;
        return Objects.equals(id, subsidy.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
