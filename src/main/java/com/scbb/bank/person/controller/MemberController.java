package com.scbb.bank.person.controller;

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
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(modifyResources(memberService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Member> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modifyResource(memberService.findById(id)));
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<Member> persist(@RequestBody Member member) {
        return ResponseEntity.ok(modifyResource(memberService.persist(member)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        memberService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("search")
    public ResponseEntity<List<Member>> search(@RequestBody Member member) {
        return ResponseEntity.ok(modifyResources(memberService.search(member)));
    }

    public Member modifyResource(Member member) {
        if (member.getTeam() != null) {
            member.getTeam().setMemberSet(null);
            member.getTeam().setSociety(null);
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
        return member;
    }

    public List<Member> modifyResources(List<Member> memberList) {
        return memberList
                .stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }


}
