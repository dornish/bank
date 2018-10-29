package com.scbb.bank.person.service;

import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.service.AccountService;
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
	private AccountService accountService;

	public MemberService(MemberRepository memberRepository, AccountService accountService) {
		this.memberRepository = memberRepository;
		this.accountService = accountService;
	}


	@Transactional
	public List<Member> findAll() {
		return memberRepository.findAll();
	}

	@Transactional
	public List<Member> findAllByTeamId(Integer id) {
		return memberRepository.findAllByTeamId(id);
	}

	@Transactional
	public Member findById(Integer id) {
		return memberRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Member having id " + id + " cannot find"));
	}

	@Transactional
	public Member persist(Member member) {
		if (member.getSubsidy() != null) {
			Subsidy subsidy = member.getSubsidy();
			subsidy.setMember(member);
		}
		if (member.getId() == null)
			accountService.persist(member.getShareAccount());
		return memberRepository.save(member);
	}

	@Transactional
	public void delete(Integer id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Member not found with having id: " + id));

		if (member.getBoardMember() != null)
			member.getBoardMember().setMember(null);
		memberRepository.delete(member);
	}

	@Transactional
	public List<Member> search(Member member) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Member> example = Example.of(member, matcher);
		return memberRepository.findAll(example);
	}
}
