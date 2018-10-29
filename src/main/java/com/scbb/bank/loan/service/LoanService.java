package com.scbb.bank.loan.service;

import com.scbb.bank.authentication.security.JwtTokenProvider;
import com.scbb.bank.exception.ResourceNotFoundException;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.*;
import com.scbb.bank.ledger.model.enums.EntryType;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.service.AccountService;
import com.scbb.bank.ledger.service.TransactionService;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.model.enums.LoanReleaseType;
import com.scbb.bank.loan.payload.InstallmentScheduleRequest;
import com.scbb.bank.loan.payload.InstallmentScheduleResponse;
import com.scbb.bank.loan.payload.LoanStatusRequest;
import com.scbb.bank.loan.payload.LoanStatusResponse;
import com.scbb.bank.loan.repository.LoanRepository;
import com.scbb.bank.person.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.scbb.bank.util.Operator.*;

@Service
public class LoanService implements AbstractService<Loan, Integer> {

	private LoanRepository loanRepository;
	private AccountRepository accountRepository;
	private AccountService accountService;
	private TransactionService transactionService;
	private JwtTokenProvider tokenProvider;

	public LoanService(LoanRepository loanRepository, AccountRepository accountRepository, AccountService accountService, TransactionService transactionService, JwtTokenProvider tokenProvider) {
		this.loanRepository = loanRepository;
		this.accountRepository = accountRepository;
		this.accountService = accountService;
		this.transactionService = transactionService;
		this.tokenProvider = tokenProvider;
	}

	@Transactional
	public List<Loan> findAll() {
		return loanRepository.findAll();
	}

	@Transactional
	public List<Loan> findAllByMemberId(Integer id) {
		return loanRepository.findAllByMemberIdAndIsApprovedIsTrue(id);
	}

	@Transactional
	public Loan findById(Integer id) {
		return loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan having id " + id + " cannot find"));
	}

	@Transactional
	public Loan persist(Loan loan) {
		loan.setRequestedDate(LocalDate.now());
		return loanRepository.save(loan);
	}

	@Transactional
	public void delete(Integer id) {
		loanRepository.delete(
				loanRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Loan having id " + id + " cannot find"))
		);
	}

	@Transactional
	public List<Loan> search(Loan loan) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Loan> example = Example.of(loan, matcher);
		return loanRepository.findAll(example);
	}


	@Transactional
	public Loan approve(Integer id) {
		Loan loan = loanRepository.getOne(id);
		loan.setIsApproved(true);
		loan.setGrantedDate(LocalDate.now());

		Account account = new Account(
				loan.getLoanType().getName() + loan.getMember().getFullName(),
				OperationType.Debit,
				new AccountType(1),
				new SubAccountType(3, "3"));
		loan.setAccount(accountService.persist(account));
		return loanRepository.save(loan);
	}

	@Transactional
	public Loan release(Integer id, LoanReleaseType releaseType, String accountNumber, String token) {
		Transaction transaction = new Transaction(LocalDateTime.now(), EntryType.Transaction_Entry, new User(tokenProvider.getUserIdFromToken(token.substring(7))));
		Loan loan = loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan having id " + id + " cannot find"));
		Entry loanEntry = new Entry(new Account(loan.getAccount().getId()), loan.getRequestedAmount(), OperationType.Debit);
		Entry entry2 = new Entry(loan.getRequestedAmount(), OperationType.Credit);
		switch (releaseType) {
			case BANK:
				entry2.setAccount(new Account(10));
				break;
			case CASH:
				entry2.setAccount(new Account(1));
				break;
			case ACCOUNT:
				entry2.setAccount(new Account(accountRepository.findByNumber(accountNumber)
						.orElseThrow(() -> new ResourceNotFoundException("Account having number " + accountNumber + " cannot find")).getId()));
		}
		transaction.getEntryList().add(loanEntry);
		transaction.getEntryList().add(entry2);
		transactionService.record(transaction);

		loan.setIsReleased(true);
		return loanRepository.save(loan);
	}

	@Transactional
	public LoanStatusResponse calculateInterest(LoanStatusRequest loanStatusRequest) {
		Account account = accountRepository.findByNumber(loanStatusRequest.getAccountNumber())
				.orElseThrow(() -> new ResourceNotFoundException("account having number" + loanStatusRequest.getAccountNumber() + " cannot find"));
		BigDecimal interest = mul(account.getBalance(), getMonthlyInterestRate(account.getLoan().getLoanType().getInterestRate()));
		return new LoanStatusResponse(interest, sub(loanStatusRequest.getAmount(), interest));
	}

	@Transactional
	public BigDecimal nextInstallmentAmount(Integer id) {
		Loan loan = loanRepository.getOne(id);
		BigDecimal currentPeriod = new BigDecimal(Long.valueOf(loan.getGrantedDate().until(LocalDate.now(), ChronoUnit.MONTHS)).toString());
		if (currentPeriod.equals(new BigDecimal("1")))
			return new BigDecimal(loan.getEquatedMonthlyValue().toString());

		BigDecimal paidAmount = new BigDecimal("0");
		List<Transaction> transactionList = transactionService.findAllByEntryAccountNumber(loan.getAccount().getNumber());
		for (Transaction transaction : transactionList) {
			for (Entry entry : transaction.getEntryList()) {
				if (entry.getAccount().getId().equals(1) && entry.getOperationType() == OperationType.Debit) { // checking cash credited entry
					paidAmount = add(paidAmount, entry.getAmount());
				}
			}
		}
		return sub(mul(loan.getEquatedMonthlyValue(), currentPeriod), paidAmount);
	}

	@Transactional
	public List<InstallmentScheduleResponse> calculateInstallmentSchedule(InstallmentScheduleRequest request) {
		BigDecimal emi = calculateEMI(request.getAmount(), request.getDuration(), request.getInterestRate());

		List<InstallmentScheduleResponse> list = new ArrayList<>();
		for (Integer value = 1; value <= request.getDuration(); value++) {
			InstallmentScheduleResponse response = new InstallmentScheduleResponse();
			response.setTotal(emi);
			response.setDate(LocalDate.now().plusMonths(value - 1));
			if (value.equals(1)) {
				BigDecimal interest = mul(request.getAmount(), getMonthlyInterestRate(request.getInterestRate()));
				response.setInterest(interest);
				response.setPrincipal(sub(emi, response.getInterest()));
				list.add(response);
				continue;
			}

			BigDecimal remainingAmount = request.getAmount();
			if (!list.isEmpty())
				for (InstallmentScheduleResponse installmentScheduleResponse : list)
					remainingAmount = sub(remainingAmount, installmentScheduleResponse.getPrincipal());

			if (value.equals(request.getDuration())) {
				response.setPrincipal(remainingAmount);
				response.setInterest(sub(emi, remainingAmount));
				list.add(response);
				continue;
			}
			BigDecimal interest = mul(remainingAmount, getMonthlyInterestRate(request.getInterestRate()));
			response.setInterest(interest);
			response.setPrincipal(sub(response.getTotal(), response.getInterest()));
			list.add(response);
		}
		return list;
	}

	@Transactional
	public BigDecimal calculateArrears(Integer id) {
		BigDecimal arrears = sub(nextInstallmentAmount(id), loanRepository.getOne(id).getEquatedMonthlyValue());
		if (arrears.compareTo(new BigDecimal("0")) < 1)
			return new BigDecimal("0");
		return arrears;
	}

	private BigDecimal getMonthlyInterestRate(BigDecimal interestRate) {
		return div(div(interestRate, new BigDecimal("100")), new BigDecimal("12"));
	}

	private BigDecimal calculateEMI(BigDecimal amount, Integer duration, BigDecimal interestRate) {
		BigDecimal monthlyInterestRate = getMonthlyInterestRate(interestRate);

		BigDecimal dividend = add(new BigDecimal("1"), monthlyInterestRate).pow(duration).multiply(monthlyInterestRate).multiply(amount);

		BigDecimal divisor = (new BigDecimal("1").add(monthlyInterestRate).pow(duration)).subtract(new BigDecimal("1"));

		return div(dividend, divisor);
	}

	private BigDecimal calcInterestForDays(LocalDate today, Integer numOfDays, BigDecimal monthlyInterest) {
		return mul(div(monthlyInterest, new BigDecimal(String.valueOf(today.lengthOfMonth()))), new BigDecimal(numOfDays.toString()));
	}

	@Scheduled(cron = "0 30 3 * * ?")
	public void interestEntry() {
		if (LocalDate.now().getDayOfMonth() != LocalDate.now().lengthOfMonth())
			return;
		loanRepository.findAll().stream()
				.filter(Loan::getIsApproved)
				.forEach(loan -> {

					LocalDate grantedDate = loan.getGrantedDate();
					LocalDate today = LocalDate.now();
					LocalDate lastDateOfLoan = loan.getGrantedDate().plusMonths(loan.getDuration());
					BigDecimal monthlyInterestRate = getMonthlyInterestRate(loan.getLoanType().getInterestRate());
					BigDecimal interest;

					// calculate interest based on granted date
					if (grantedDate.getYear() == today.getYear() && grantedDate.getMonth() == today.getMonth()) {
						int numOfDays = today.lengthOfMonth() - loan.getGrantedDate().getDayOfMonth();
						interest = calcInterestForDays(today, numOfDays, mul(loan.getAccount().getBalance(), monthlyInterestRate));
					} else if (lastDateOfLoan.getYear() == today.getYear() && lastDateOfLoan.getMonth() == today.getMonth())
						interest = calcInterestForDays(today, today.getDayOfMonth(), mul(loan.getAccount().getBalance(), monthlyInterestRate));
					else
						interest = mul(loan.getAccount().getBalance(), monthlyInterestRate);

					Transaction transaction = new Transaction(LocalDateTime.now(), EntryType.Transaction_Entry, new User(1));
					Entry income = new Entry(new Account(2), interest, OperationType.Credit);
					Entry receivable = new Entry(new Account(3), interest, OperationType.Debit);
					transaction.getEntryList().add(income);
					transaction.getEntryList().add(receivable);
					transactionService.record(transaction);
				});
	}


}
