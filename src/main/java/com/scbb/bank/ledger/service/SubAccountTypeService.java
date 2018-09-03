package com.scbb.bank.ledger.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.SubAccountType;
import com.scbb.bank.ledger.repository.SubAccountTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubAccountTypeService implements AbstractService<SubAccountType, Integer> {

    private SubAccountTypeRepository subAccountTypeRepository;

    public SubAccountTypeService(SubAccountTypeRepository subAccountTypeRepository) {
        this.subAccountTypeRepository = subAccountTypeRepository;
    }

    @Transactional
    public List<SubAccountType> findAll() {
        return subAccountTypeRepository.findAll();
    }

    @Transactional
    public SubAccountType findById(Integer id) {
        return subAccountTypeRepository.getOne(id);
    }

    @Override
    public SubAccountType persist(SubAccountType subAccountType) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<SubAccountType> search(SubAccountType subAccountType) {
        return null;
    }

    public List<SubAccountType> findAllByAccountTypeId(Integer id) {
        return subAccountTypeRepository.findAllByAccountTypeId(id);
    }
}
