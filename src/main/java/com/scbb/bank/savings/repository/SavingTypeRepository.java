package com.scbb.bank.savings.repository;

import com.scbb.bank.savings.model.SavingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingTypeRepository extends JpaRepository<SavingType, Integer> {
}
