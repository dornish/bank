package com.scbb.bank.area.model;

import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.person.model.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "team")
	private List<Member> memberList = new ArrayList<>();

	@ManyToOne
	private Society society;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Account account;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Team)) return false;
		Team team = (Team) o;
		return Objects.equals(id, team.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}
