package com.scbb.bank.area.repository;

import com.scbb.bank.area.model.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyRepository extends JpaRepository<Society, Integer> {

	List<Society> findAllByDivisionId(Integer id);
}
