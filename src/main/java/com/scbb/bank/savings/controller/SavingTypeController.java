package com.scbb.bank.savings.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.savings.model.SavingType;
import com.scbb.bank.savings.service.SavingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("savingTypes")
public class SavingTypeController implements AbstractController<SavingType, Integer> {

    private SavingTypeService savingTypeService;

    public SavingTypeController(SavingTypeService savingTypeService) {
        this.savingTypeService = savingTypeService;
    }

    @GetMapping
    public List<SavingType> findAll() {
        return savingTypeService.findAll();
    }

    @GetMapping("{id}")
    public SavingType findById(@PathVariable Integer id) {
        return savingTypeService.findById(id);
    }

    @Override
    public SavingType persist(SavingType savingType) {
        return null;
    }

    @Override
    public ResponseEntity delete(Integer id) {
        return null;
    }

    @Override
    public List<SavingType> search(SavingType savingType) {
        return null;
    }

    @Override
    public SavingType modifyResource(SavingType savingType) {
        return null;
    }

    @Override
    public List<SavingType> modifyResources(List<SavingType> savingTypes) {
        return null;
    }
}
