package com.scbb.bank.person.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.person.model.Member;
import com.scbb.bank.person.model.Subsidy;
import com.scbb.bank.person.repository.MemberRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService implements AbstractService<Member, Integer> {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public List<Member> findAllByTeamId(Integer id) {
        return memberRepository.findAllByTeamId(id);
    }

    @Override
    public Member findById(Integer id) {
        return memberRepository.getOne(id);
    }

    @Transactional
    @Override
    public Member persist(Member member) {
        if (member.getSubsidy() != null) {
            Subsidy subsidy = member.getSubsidy();
            subsidy.setMember(member);
        }
        return memberRepository.save(member);
    }

    @Transactional
    @Override
    public boolean delete(Integer id) {
        Member member = memberRepository.getOne(id);
        if (member.getBoardMember() != null)
            member.getBoardMember().setMember(null);
        memberRepository.delete(member);
        return false;
    }

    @Override
    public List<Member> search(Member member) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Member> example = Example.of(member, matcher);
        return memberRepository.findAll(example);
    }
}
