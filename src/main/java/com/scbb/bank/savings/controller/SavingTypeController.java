package com.scbb.bank.savings.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.savings.model.SavingType;
import com.scbb.bank.savings.service.SavingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
		return modifyResources(savingTypeService.findAll());
	}

	@GetMapping("{id}")
	public SavingType findById(@PathVariable Integer id) {
		return modifyResource(savingTypeService.findById(id));
	}

	@Override
	public SavingType persist(SavingType savingType) {
		return modifyResource(savingTypeService.persist(savingType));
	}

	@Override
	public ResponseEntity<String> delete(Integer id) {
		return null;
	}

	@Override
	public List<SavingType> search(SavingType savingType) {
		return null;
	}

	@Override
	public SavingType modifyResource(SavingType savingType) {
		if (savingType.getSavingsList() != null) {
			savingType.getSavingsList().forEach(savings -> {
				savings.setSavingType(null);
				savings.setMember(null);
				savings.setAccount(null);
			});
		}
		return savingType;
	}

	@Override
	public List<SavingType> modifyResources(List<SavingType> savingTypes) {
		return savingTypes.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}
