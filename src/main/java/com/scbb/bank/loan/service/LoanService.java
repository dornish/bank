package com.scbb.bank.loan.service;

import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.Transaction;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.ledger.service.TransactionService;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.payload.InstallmentScheduleRequest;
import com.scbb.bank.loan.payload.InstallmentScheduleResponse;
import com.scbb.bank.loan.payload.LoanStatusRequest;
import com.scbb.bank.loan.payload.LoanStatusResponse;
import com.scbb.bank.loan.repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService implements AbstractService<Loan, Integer> {

    private LoanRepository loanRepository;
    private AccountRepository accountRepository;
    private TransactionService transactionService;

    public LoanService(LoanRepository loanRepository, AccountRepository accountRepository, TransactionService transactionService) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Transactional
    public Loan findById(Integer id) {
        return loanRepository.getOne(id);
    }

    @Transactional
    public Loan persist(Loan loan) {
        return loanRepository.save(loan);
    }

    @Transactional
    public boolean delete(Integer id) {
        loanRepository.deleteById(id);
        return true;
    }

    @Transactional
    public List<Loan> search(Loan loan) {
        return null;
    }

    @Transactional
    public Loan payInstallment(Integer id) {
        Loan existingLoan = loanRepository.getOne(id);
        return loanRepository.save(existingLoan);
    }

    @Transactional
    public Loan approve(Integer id) {
        Loan loan = loanRepository.getOne(id);
        loan.setIsApproved(true);
        loan.setGrantedDate(LocalDate.now());
        return loanRepository.save(loan);
    }

    @Transactional
    public LoanStatusResponse calculateInterest(LoanStatusRequest loanStatusRequest) {
        Account account = accountRepository.findByNumber(loanStatusRequest.getAccountNumber());
        Loan loan = account.getLoan();
        BigDecimal interest = mul(account.getBalance(), getMonthlyInterestRate(loan.getLoanType().getInterestRate()));

        /*BigDecimal fine = new BigDecimal("0");
        if (loan.getNextInstallmentDate().isBefore(LocalDate.now())) {
            int days = loan.getNextInstallmentDate().until(LocalDate.now()).getDays();
            if (days / 7 > 0) {
                int f = ((days / 7) * 500);
                fine = new BigDecimal(Integer.toString(f));
            }
        }*/

        return new LoanStatusResponse(interest, sub(loanStatusRequest.getAmount(), interest));
    }

    public BigDecimal remainingInstallmentAmount(Integer id) {
        Loan loan = loanRepository.getOne(id);
        if (loan.getCurrentPeriod().equals(1))
            return new BigDecimal(loan.getEquatedMonthlyValue().toString());

        BigDecimal paidAmount = new BigDecimal("0");

        List<Transaction> transactionList = transactionService.findAllByAccountId(loan.getAccount().getNumber());
        for (Transaction transaction : transactionList) {
            for (Entry entry : transaction.getEntryList()) {
                if (entry.getAccount().getNumber().equals("5")) {
                    paidAmount = add(paidAmount, entry.getAmount());
                }
            }
        }
        return sub(mul(loan.getEquatedMonthlyValue(), new BigDecimal(loan.getCurrentPeriod().toString())), paidAmount);
    }

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
            if (!list.isEmpty()) {
                for (InstallmentScheduleResponse installmentScheduleResponse : list) {
                    remainingAmount = sub(remainingAmount, installmentScheduleResponse.getPrincipal());
                }
            }

            if (value.equals(request.getDuration())) {
                response.setPrincipal(remainingAmount);
                response.setInterest(emi.subtract(remainingAmount));
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

    private BigDecimal getMonthlyInterestRate(BigDecimal interestRate) {
        BigDecimal decimal = div(interestRate, new BigDecimal("100"));
        return div(decimal, new BigDecimal("12"));
    }

    private BigDecimal calculateEMI(BigDecimal amount, Integer duration, BigDecimal interestRate) {
        BigDecimal monthlyInterestRate = getMonthlyInterestRate(interestRate);

        BigDecimal dividend = (new BigDecimal("1").add(monthlyInterestRate)).pow(duration).multiply(monthlyInterestRate).multiply(amount);

        BigDecimal divisor = (new BigDecimal("1").add(monthlyInterestRate).pow(duration)).subtract(new BigDecimal("1"));

        return dividend.divide(divisor, 2, RoundingMode.HALF_EVEN);
    }


    private BigDecimal mul(BigDecimal operand1, BigDecimal operand2) {
        return operand1.multiply(operand2).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal div(BigDecimal operand1, BigDecimal operand2) {
        return operand1.divide(operand2, 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal add(BigDecimal operand1, BigDecimal operand2) {
        return operand1.add(operand2).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal sub(BigDecimal operand1, BigDecimal operand2) {
        return operand1.subtract(operand2).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Scheduled(cron = "0 30 23 * * ?")
    public void increasePeriod() {
        loanRepository.findAll().stream().parallel()
                .forEach(loan -> {
                    if (LocalDate.now().getDayOfMonth() == loan.getGrantedDate().getDayOfMonth()) {
                        loan.setCurrentPeriod(loan.getCurrentPeriod() + 1);
                    }
                });
    }


}
