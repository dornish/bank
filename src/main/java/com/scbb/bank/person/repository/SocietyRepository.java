package com.scbb.bank.person.repository;

import com.scbb.bank.person.model.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyRepository extends JpaRepository<Society, Integer> {

    List<Society> findAllByDivisionId(Integer id);
}
