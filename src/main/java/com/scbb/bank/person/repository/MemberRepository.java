package com.scbb.bank.person.repository;

import com.scbb.bank.person.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	List<Member> findAllByTeamId(Integer id);
}
