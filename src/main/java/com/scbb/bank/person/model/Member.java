package com.scbb.bank.person.model;

import com.scbb.bank.area.model.Team;
import com.scbb.bank.person.model.enums.Gender;
import com.scbb.bank.person.model.enums.IncomeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;
    private String address;
    private String nic;
    private LocalDate dob;
    private String telephone;
    private String spouse;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private IncomeType incomeType;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Subsidy subsidy;

    @OneToOne(mappedBy = "member") //Parent
    private BoardMember boardMember;

    @ManyToOne
    private Team team;

    public Member(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
