package com.scbb.bank.savings.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.savings.model.SavingType;
import com.scbb.bank.savings.repository.SavingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavingTypeService implements AbstractService<SavingType, Integer> {

    private SavingTypeRepository savingTypeRepository;

    public SavingTypeService(SavingTypeRepository savingTypeRepository) {
        this.savingTypeRepository = savingTypeRepository;
    }

    @Transactional
    public List<SavingType> findAll() {
        return savingTypeRepository.findAll();
    }

    @Transactional
    public SavingType findById(Integer id) {
        return savingTypeRepository.getOne(id);
    }

    @Transactional
    public SavingType persist(SavingType savingType) {
        return savingTypeRepository.save(savingType);
    }

    @Transactional
    public boolean delete(Integer id) {
        try {
            savingTypeRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public List<SavingType> search(SavingType savingType) {
        return null;
    }
}
