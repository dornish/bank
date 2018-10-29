package com.scbb.bank.area.repository;

import com.scbb.bank.area.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

	List<Team> findAllBySocietyId(Integer id);

	@Query(value = "select t.name as team, count(m) as count from Team t join Member m on t = m.team group by t")
	List<Report> report();

}
