package com.scbb.bank.savings.repository;

import com.scbb.bank.savings.model.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsRepository extends JpaRepository<Savings, Integer> {

	List<Savings> findAllByMemberId(Integer id);
}
