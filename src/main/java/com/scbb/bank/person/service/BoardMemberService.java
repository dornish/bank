package com.scbb.bank.person.service;

import com.scbb.bank.person.model.BoardMember;
import com.scbb.bank.person.repository.BoardMemberRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardMemberService implements AbstractService<BoardMember, Integer> {

    private BoardMemberRepository boardMemberRepository;

    public BoardMemberService(BoardMemberRepository boardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
    }

    @Override
    public List<BoardMember> findAll() {
        return boardMemberRepository.findAll();
    }

    public List<BoardMember> findAllByDivisionIsNull() {
        return boardMemberRepository.findAllByDivisionIsNull();
    }

    @Override
    public BoardMember findById(Integer id) {
        return boardMemberRepository.getOne(id);
    }

    @Transactional
    @Override
    public BoardMember persist(BoardMember boardMember) {
        return boardMemberRepository.save(boardMember);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        BoardMember boardMember = boardMemberRepository.getOne(id);
        if (boardMember.getUser() != null)
            boardMember.getUser().setBoardMember(null);
        if (boardMember.getDivision() != null)
            boardMember.getDivision().setBoardMember(null);
        if (!boardMember.getAttendanceList().isEmpty())
            boardMember.getAttendanceList().forEach(attendance -> attendance.setBoardMember(null));
        boardMemberRepository.delete(boardMember);

    }

    @Override
    public List<BoardMember> search(BoardMember boardMember) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<BoardMember> example = Example.of(boardMember, matcher);
        return boardMemberRepository.findAll(example);
    }
}
