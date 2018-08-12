package com.scbb.bank.person.repository;

import com.scbb.bank.person.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {}
