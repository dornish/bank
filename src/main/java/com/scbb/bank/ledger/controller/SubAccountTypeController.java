package com.scbb.bank.ledger.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.ledger.model.SubAccountType;
import com.scbb.bank.ledger.service.SubAccountTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("subAccountTypes")
public class SubAccountTypeController implements AbstractController<SubAccountType, Integer> {

    private SubAccountTypeService subAccountTypeService;

    public SubAccountTypeController(SubAccountTypeService subAccountTypeService) {
        this.subAccountTypeService = subAccountTypeService;
    }

    @GetMapping
    public List<SubAccountType> findAll() {
        return modifyResources(subAccountTypeService.findAll());
    }

    @GetMapping("accountType/{id}")
    public List<SubAccountType> findAllByAccountTypeId(@PathVariable Integer id) {
        return modifyResources(subAccountTypeService.findAllByAccountTypeId(id));
    }


    @GetMapping("{id}")
    public SubAccountType findById(@PathVariable Integer id) {
        return modifyResource(subAccountTypeService.findById(id));
    }

    @Override
    public SubAccountType persist(SubAccountType subAccountType) {
        return null;
    }

    @Override
    public ResponseEntity delete(Integer id) {
        return null;
    }

    @Override
    public List<SubAccountType> search(SubAccountType subAccountType) {
        return null;
    }

    @Override
    public SubAccountType modifyResource(SubAccountType subAccountType) {
        if (subAccountType.getAccountType() != null) {
            subAccountType.getAccountType().setSubAccountTypeList(null);
            subAccountType.getAccountType().setAccountList(null);
        }
        if (!subAccountType.getAccountList().isEmpty()) {
            subAccountType.getAccountList().forEach(account -> {
                account.setShareHolder(null);
                account.setAccountType(null);
                account.setSubAccountType(null);
                account.setTeam(null);
                account.setSavings(null);
            });
        }
        return subAccountType;
    }

    @Override
    public List<SubAccountType> modifyResources(List<SubAccountType> subAccountTypes) {
        return subAccountTypes.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}
