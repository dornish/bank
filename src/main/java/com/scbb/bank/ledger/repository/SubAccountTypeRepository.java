package com.scbb.bank.ledger.repository;

import com.scbb.bank.ledger.model.SubAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubAccountTypeRepository extends JpaRepository<SubAccountType, Integer> {

	List<SubAccountType> findAllByAccountTypeId(Integer id);
}
