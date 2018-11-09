package com.scbb.bank.savings.service;

import com.scbb.bank.exception.ResourceCannotDeleteException;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.service.AccountService;
import com.scbb.bank.ledger.service.EntryService;
import com.scbb.bank.ledger.service.TransactionService;
import com.scbb.bank.loan.payload.report.DataSet;
import com.scbb.bank.loan.payload.report.PieChart;
import com.scbb.bank.savings.model.SavingType;
import com.scbb.bank.savings.model.Savings;
import com.scbb.bank.savings.payload.SavingsReport;
import com.scbb.bank.savings.repository.SavingsRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static com.scbb.bank.util.Operator.add;
import static com.scbb.bank.util.Operator.sub;

@Service
public class SavingsService implements AbstractService<Savings, Integer> {

	private SavingsRepository savingsRepository;
	private SavingTypeService savingTypeService;
	private AccountService accountService;
	private TransactionService transactionService;
	private EntryService entryService;

	public SavingsService(SavingsRepository savingsRepository, SavingTypeService savingTypeService, AccountService accountService, TransactionService transactionService, EntryService entryService) {
		this.savingsRepository = savingsRepository;
		this.savingTypeService = savingTypeService;
		this.accountService = accountService;
		this.transactionService = transactionService;
		this.entryService = entryService;
	}

	@Transactional
	public List<Savings> findAll() {
		return savingsRepository.findAll();
	}

	@Transactional
	public List<Savings> findAllByMemberId(Integer id) {
		return savingsRepository.findAllByMemberId(id);
	}

	@Transactional
	public Savings findById(Integer id) {
		return savingsRepository.getOne(id);
	}

	@Transactional
	public Savings persist(Savings savings) {
		if (savings.getId() == null) {
			accountService.persist(savings.getAccount());
			savings.setOpenedDate(LocalDate.now());
		}
		return savingsRepository.save(savings);
	}

	@Transactional
	public void delete(Integer id) {
		Savings savings = savingsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource having id " + id + " cannot find"));
		if (savings.getAccount().getBalance().compareTo(new BigDecimal("0")) != 0) {
			throw new ResourceCannotDeleteException("Savings having id " + id + " cannot be deleted");
		}
		savingsRepository.deleteById(id);
	}

	@Transactional
	public List<Savings> search(Savings savings) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

		Example<Savings> example = Example.of(savings, matcher);

		return savingsRepository.findAll(example);
	}

	@Transactional
	public SavingsReport getAllSavingsByType() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime monthBefore = LocalDateTime.now().minusMonths(1);
		SavingsReport savingsReport = new SavingsReport();
		DataSet thisMonth = new DataSet();
		thisMonth.setLabel(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));
		DataSet previousMonth = new DataSet();
		previousMonth.setLabel(now.minusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));

		for (SavingType savingType : savingTypeService.findAll()) {
			savingsReport.getLabelList().add(savingType.getName());
			BigDecimal thisMonthSum = new BigDecimal("0");
			BigDecimal previousMonthSum = new BigDecimal("0");
			for (Savings savings : savingType.getSavingsList()) {
				for (Entry entry : entryService.findAllByAccountNumber(savings.getAccount().getNumber(), LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0), now)) {
					if (entry.getAccount().getId().equals(savings.getAccount().getId()))
						thisMonthSum = entry.getOperationType() == OperationType.Credit ? add(thisMonthSum, entry.getAmount()) : sub(thisMonthSum, entry.getAmount());
				}
				for (Entry entry : entryService.findAllByAccountNumber(savings.getAccount().getNumber(),
						LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), 1, 0, 0),
						LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), monthBefore.toLocalDate().lengthOfMonth(), 0, 0))) {
					if (entry.getAccount().getId().equals(savings.getAccount().getId()))
						previousMonthSum = entry.getOperationType() == OperationType.Credit ? add(previousMonthSum, entry.getAmount()) : sub(previousMonthSum, entry.getAmount());
				}
			}
			thisMonth.getData().add(thisMonthSum);
			thisMonth.getData().add(previousMonthSum);
		}
		savingsReport.getDataSetList().add(thisMonth);
		savingsReport.getDataSetList().add(previousMonth);

		return savingsReport;
	}

	@Transactional
	public PieChart getAllUsingPieChart() {
		PieChart pieChart = new PieChart();
		for (SavingType savingType : savingTypeService.findAll()) {
			pieChart.getLabelList().add(savingType.getName() + " - " + savingType.getInterestRate() + "%");
			BigDecimal total = new BigDecimal("0");
			for (Savings savings : savingType.getSavingsList()) {
				total = add(total, savings.getAccount().getBalance());
			}
			pieChart.getDataList().add(total);
		}
		return pieChart;
	}
}
