package com.scbb.bank.area.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Society {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "society")
	private List<Team> teamList = new ArrayList<>();

	@ManyToOne
	private Division division;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Society)) return false;

		Society society = (Society) o;

		return id != null ? id.equals(society.id) : society.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
