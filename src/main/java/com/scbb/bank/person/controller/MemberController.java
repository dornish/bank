package com.scbb.bank.person.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.person.model.Member;
import com.scbb.bank.person.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/members")
public class MemberController implements AbstractController<Member, Integer> {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> findAll() {
        return modifyResources(memberService.findAll());
    }

    @GetMapping("teams/{id}")
    public List<Member> findAllByTeamId(@PathVariable Integer id) {
        return modifyResources(memberService.findAllByTeamId(id));
    }

    @GetMapping("{id}")
    public Member findById(@PathVariable Integer id) {
        return modifyResource(memberService.findById(id));
    }

    @PostMapping
    @PutMapping
    public Member persist(@RequestBody Member member) {
        return modifyResource(memberService.persist(member));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        memberService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("search")
    public List<Member> search(@RequestBody Member member) {
        return modifyResources(memberService.search(member));
    }

    public Member modifyResource(Member member) {
        if (member.getTeam() != null) {
            member.getTeam().setMemberList(null);
            member.getTeam().setSociety(null);
            member.getTeam().setAccount(null);
        }
        if (member.getBoardMember() != null) {
            member.getBoardMember().setDivision(null);
            member.getBoardMember().setMember(null);
            member.getBoardMember().setUser(null);
            member.getBoardMember().setAttendanceList(null);
        }

        if (member.getSubsidy() != null) {
            member.getSubsidy().setMember(null);
        }

        if (member.getShareAccount() != null) {
            member.getShareAccount().setTeam(null);
            member.getShareAccount().setSubAccountType(null);
            member.getShareAccount().setAccountType(null);
            member.getShareAccount().setShareHolder(null);
            member.getShareAccount().setSavings(null);
            member.getShareAccount().setLoan(null);
        }
        return member;
    }

    public List<Member> modifyResources(List<Member> memberList) {
        return memberList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }


}
