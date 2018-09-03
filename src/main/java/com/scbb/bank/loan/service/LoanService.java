package com.scbb.bank.loan.service;

import com.scbb.bank.authentication.security.JwtTokenProvider;
import com.scbb.bank.interfaces.AbstractService;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.repository.AccountRepository;
import com.scbb.bank.loan.model.Loan;
import com.scbb.bank.loan.payload.LoanStatusRequest;
import com.scbb.bank.loan.payload.LoanStatusResponse;
import com.scbb.bank.loan.repository.LoanRepository;
import com.scbb.bank.person.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService implements AbstractService<Loan, Integer> {

    private LoanRepository loanRepository;
    private AccountRepository accountRepository;
    private JwtTokenProvider tokenProvider;
    private UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, AccountRepository accountRepository, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
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
    public LoanStatusResponse calculateInterestAndFine(LoanStatusRequest loanStatusRequest) {
        Account account = accountRepository.findByNumber(loanStatusRequest.getAccountNumber());
        Loan loan = account.getLoan();
        BigDecimal interest = account.getBalance().multiply(loan.getLoanType().getInterestRate()).divide(new BigDecimal("12"), 2, RoundingMode.HALF_EVEN);
        System.out.println("Interest: " + interest);
        BigDecimal fine = new BigDecimal("0");
        if (loan.getNextInstallmentDate().isBefore(LocalDate.now())) {
            int days = loan.getNextInstallmentDate().until(LocalDate.now()).getDays();
            if (days / 7 > 0) {
                int f = ((days / 7) * 500);
                fine = new BigDecimal(Integer.toString(f));
                System.out.println("fine: " + fine);
            }
            System.out.println("Days: " + days);
        }
        return new LoanStatusResponse(interest, fine, loanStatusRequest.getAmount().subtract(interest).subtract(fine));
    }

    @Transactional
    public Loan payInstallment(Integer id) {
        Loan existingLoan = loanRepository.getOne(id);
        existingLoan.setRemainingInstallments(existingLoan.getRemainingInstallments() - 1);
        existingLoan.setNextInstallmentDate(existingLoan.getNextInstallmentDate().plusMonths(1));
        return loanRepository.save(existingLoan);
    }

    @Transactional
    public Loan approve(Integer id) {
        Loan loan = loanRepository.getOne(id);
        loan.setIsApproved(true);
        return loan;

    }
}
