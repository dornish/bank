package com.scbb.bank.person.repository;


import com.scbb.bank.person.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    List<Staff> findAllByDivisionIsNull();

}
