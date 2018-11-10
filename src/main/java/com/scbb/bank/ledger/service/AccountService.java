package com.scbb.bank.ledger.service;

import com.scbb.bank.exception.ResourceCannotDeleteException;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.model.AccountType;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.SubAccountType;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.payload.AccountTypeReport;
import com.scbb.bank.ledger.payload.SubAccountTypeReport;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.repository.SubAccountTypeRepository;
import com.scbb.bank.loan.payload.report.DataSet;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.scbb.bank.util.Operator.add;
import static com.scbb.bank.util.Operator.sub;

@Service
public class AccountService implements AbstractService<Account, Integer> {

	private AccountRepository accountRepository;
	private TransactionService transactionService;
	private EntryService entryService;
	private AccountTypeService accountTypeService;
	private SubAccountTypeRepository subAccountTypeRepository;

	public AccountService(AccountRepository accountRepository, TransactionService transactionService, EntryService entryService, AccountTypeService accountTypeService, SubAccountTypeRepository subAccountTypeRepository) {
		this.accountRepository = accountRepository;
		this.transactionService = transactionService;
		this.entryService = entryService;
		this.accountTypeService = accountTypeService;
		this.subAccountTypeRepository = subAccountTypeRepository;
	}

	@Transactional
	public List<Account> findAll() {
		return accountRepository.findAll(Sort.by(Sort.Direction.ASC, "number"));
	}

	@Transactional
	public List<Account> findAllHavingSavings() {
		return accountRepository.findAllBySavingsIsNotNull();
	}


	@Transactional
	public Account findById(Integer id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account having id " + id + " cannot find"));
	}

	public Account findByNumber(String number) {
		return accountRepository.findByNumber(number)
				.orElseThrow(() -> new ResourceNotFoundException("Account having number " + number + " cannot find"));
	}

	@Transactional
	public Account persist(Account account) {
		if (account.getId() == null) {
			account.setBalance(new BigDecimal("0"));
			account.setNumber(calculateAccountNumber(account));
		} else {
			account.setNumber(accountRepository.getOne(account.getId()).getNumber());
			account.setBalance(accountRepository.getOne(account.getId()).getBalance());
		}
		return accountRepository.save(account);
	}

	@Transactional
	public void delete(Integer id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account having id" + id + " cannot find"));
		if (account.getBalance().compareTo(new BigDecimal("0")) != 0)
			throw new ResourceCannotDeleteException("Account having id " + id + " cannot be deleted");
		accountRepository.delete(account);
	}

	@Transactional
	public List<Account> search(Account account) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withMatcher("number", ExampleMatcher.GenericPropertyMatcher::startsWith);
		return accountRepository.findAll(Example.of(account, matcher), Sort.by(Sort.Direction.ASC, "number"));
	}

	private String calculateAccountNumber(Account account) {
		String subAccountTypeNumber = subAccountTypeRepository.getOne(account.getSubAccountType().getId()).getNumber();
		String number = account.getAccountType().getId().toString() + subAccountTypeNumber;
		if (accountRepository.findFirstByNumberStartsWithOrderByIdDesc(number).isPresent()) {
			Account latestAccount = accountRepository.findFirstByNumberStartsWithOrderByIdDesc(number).get();
			String value = latestAccount.getNumber().substring(2);
			if (Integer.valueOf(value, 10) < 9)
				number += "00" + (Integer.valueOf(value, 10) + 1);
			else if (Integer.valueOf(value, 10) < 99)
				number += "0" + (Integer.valueOf(value, 10) + 1);
			else
				number += Integer.valueOf(value, 10) + 1;
		} else {
			number += "000";
		}
		return number;
	}

	@Transactional
	public List<DataSet> sharesReport() {
		List<DataSet> dataSetList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime monthBefore = now.minusMonths(1);
		DataSet thisMonth = new DataSet();
		thisMonth.setLabel(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));
		DataSet previousMonth = new DataSet();
		previousMonth.setLabel(monthBefore.getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));
		BigDecimal thisMonthSum = new BigDecimal("0");
		BigDecimal previousMonthSum = new BigDecimal("0");

		for (Account account : subAccountTypeRepository.getOne(13).getAccountList()) { // sub account id for share
			for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(),
					LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0), now)) {
				if (entry.getOperationType() == OperationType.Credit && entry.getAccount().getId().equals(account.getId()))
					thisMonthSum = add(thisMonthSum, entry.getAmount());
				else
					thisMonthSum = sub(thisMonthSum, entry.getAmount());
			}

			for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(),
					LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), 1, 0, 0),
					LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), monthBefore.toLocalDate().lengthOfMonth(), 0, 0)
			)) {
				if (entry.getOperationType() == OperationType.Credit && entry.getAccount().getId().equals(account.getId()) && entry.getAccount().getId().equals(account.getId()))
					previousMonthSum = add(previousMonthSum, entry.getAmount());
				else
					previousMonthSum = sub(previousMonthSum, entry.getAmount());
			}

		}
		thisMonth.getData().add(thisMonthSum);
		previousMonth.getData().add(previousMonthSum);
		dataSetList.add(thisMonth);
		dataSetList.add(previousMonth);
		return dataSetList;
	}

	@Transactional
	public List<DataSet> teamReport() {
		List<DataSet> dataSetList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime monthBefore = now.minusMonths(1);
		DataSet thisMonth = new DataSet();
		thisMonth.setLabel(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));
		DataSet previousMonth = new DataSet();
		previousMonth.setLabel(now.minusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ROOT));
		BigDecimal thisMonthSum = new BigDecimal("0");
		BigDecimal previousMonthSum = new BigDecimal("0");

		for (Account account : subAccountTypeRepository.getOne(14).getAccountList()) { // sub account id for team
			System.out.println("account " + account.getId());
			for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(),
					LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0), now)) {
				System.out.println("this month transaction" + entry.getTransaction().getId());
				if (entry.getOperationType() == OperationType.Credit)
					thisMonthSum = add(thisMonthSum, entry.getAmount());
				else
					thisMonthSum = sub(thisMonthSum, entry.getAmount());
			}

			for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(),
					LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), 1, 0, 0),
					LocalDateTime.of(monthBefore.getYear(), monthBefore.getMonth(), monthBefore.toLocalDate().lengthOfMonth(), 0, 0)
			)) {
				System.out.println("previous month transaction" + entry.getTransaction().getId());
				if (entry.getOperationType() == OperationType.Credit)
					previousMonthSum = add(previousMonthSum, entry.getAmount());
				else
					previousMonthSum = sub(previousMonthSum, entry.getAmount());
			}

		}
		thisMonth.getData().add(thisMonthSum);
		previousMonth.getData().add(previousMonthSum);
		dataSetList.add(thisMonth);
		dataSetList.add(previousMonth);
		return dataSetList;
	}

	@Transactional
	public List<AccountTypeReport> incomeStatement(LocalDateTime from, LocalDateTime to) {
		List<AccountTypeReport> accountTypeReportList = new ArrayList<>();
		for (AccountType accountType : accountTypeService.findAll()) {

			if (accountType.getName().equals("Capital") || accountType.getName().equals("Assets") || accountType.getName().equals("Liabilities"))
				continue;

			AccountTypeReport accountTypeReport = new AccountTypeReport(accountType.getName());
			BigDecimal accountTypeTotal = new BigDecimal("0");

			for (SubAccountType subAccountType : accountType.getSubAccountTypeList()) {

				SubAccountTypeReport subAccountTypeReport = new SubAccountTypeReport(subAccountType.getName());
				BigDecimal subAccountTypeTotal = new BigDecimal("0");

				for (Account account : subAccountType.getAccountList()) {

					if (from != null && to != null) { // finding with period
						for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(), from, to)) {
							if (entry.getOperationType() == account.getOperationType()) {
								accountTypeTotal = add(subAccountTypeTotal, entry.getAmount());
								subAccountTypeTotal = add(subAccountTypeTotal, entry.getAmount());
							} else {
								accountTypeTotal = sub(subAccountTypeTotal, entry.getAmount());
								subAccountTypeTotal = sub(subAccountTypeTotal, entry.getAmount());
							}
						}
					} else { // finding with last balances
						System.out.println("----------finding with last balances--------------");
						accountTypeTotal = add(accountTypeTotal, account.getBalance());
						subAccountTypeTotal = add(subAccountTypeTotal, account.getBalance());
					}
				}
				subAccountTypeReport.setBalance(subAccountTypeTotal);
				accountTypeReport.getSubList().add(subAccountTypeReport);
			}
			accountTypeReport.setBalance(accountTypeTotal);
			accountTypeReportList.add(accountTypeReport);
		}
		return accountTypeReportList;
	}

	@Transactional
	public List<AccountTypeReport> balanceSheet(LocalDateTime from, LocalDateTime to) {
		List<AccountTypeReport> accountTypeReportList = new ArrayList<>();

		for (AccountType accountType : accountTypeService.findAll()) {
			if (accountType.getName().equals("Income") || accountType.getName().equals("Expenses"))
				continue;

			AccountTypeReport accountTypeReport = new AccountTypeReport(accountType.getName());
			BigDecimal accountTypeTotal = new BigDecimal("0");

			for (SubAccountType subAccountType : accountType.getSubAccountTypeList()) {
				SubAccountTypeReport subAccountTypeReport = new SubAccountTypeReport(subAccountType.getName());
				BigDecimal subAccountTypeTotal = new BigDecimal("0");

				for (Account account : subAccountType.getAccountList()) {

					if (from != null && to != null) {
						for (Entry entry : entryService.findAllByAccountNumber(account.getNumber(), from, to)) {
							if (entry.getOperationType() == account.getOperationType()) {
								accountTypeTotal = add(subAccountTypeTotal, entry.getAmount());
								subAccountTypeTotal = add(subAccountTypeTotal, entry.getAmount());
							} else {
								accountTypeTotal = sub(subAccountTypeTotal, entry.getAmount());
								subAccountTypeTotal = sub(subAccountTypeTotal, entry.getAmount());
							}
						}
					} else {
						System.out.println("----------finding with last balances--------------");
						accountTypeTotal = add(accountTypeTotal, account.getBalance());
						subAccountTypeTotal = add(subAccountTypeTotal, account.getBalance());
					}


				}
				subAccountTypeReport.setBalance(subAccountTypeTotal);
				accountTypeReport.getSubList().add(subAccountTypeReport);

			}
			if (accountType.getName().equals("Capital")) {
				SubAccountTypeReport profitOrLoss = new SubAccountTypeReport("Profit/Loss");
				BigDecimal totalIncome = new BigDecimal("0");
				BigDecimal totalExpenses = new BigDecimal("0");

				for (AccountTypeReport typeReport : incomeStatement(from, to)) {
					if (typeReport.getName().equals("Income"))
						totalIncome = typeReport.getBalance();
					else
						totalExpenses = typeReport.getBalance();
				}
				profitOrLoss.setBalance(sub(totalIncome, totalExpenses));
				accountTypeReport.getSubList().add(profitOrLoss);
				accountTypeTotal = add(accountTypeTotal, profitOrLoss.getBalance());
			}

			accountTypeReport.setBalance(accountTypeTotal);
			accountTypeReportList.add(accountTypeReport);

		}
		return accountTypeReportList;
	}


}
