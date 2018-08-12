package com.scbb.bank.person.repository;

import com.scbb.bank.person.model.Division;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository extends JpaRepository<Division, Integer> {
}
