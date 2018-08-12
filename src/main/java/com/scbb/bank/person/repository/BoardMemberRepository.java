package com.scbb.bank.person.repository;

import com.scbb.bank.person.model.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Integer> {

    List<BoardMember> findAllByDivisionIsNull();
}
