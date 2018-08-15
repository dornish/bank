package com.scbb.bank.area.repository;

import com.scbb.bank.area.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    List<Team> findAllBySocietyId(Integer id);
}
