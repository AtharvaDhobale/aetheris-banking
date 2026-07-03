package com.am.accounting.controller;

import com.am.accounting.model.Account;
import com.am.accounting.model.LoanFacility;
import com.am.accounting.repository.AccountRepository;
import com.am.accounting.repository.LoanFacilityRepository;
import com.am.accounting.service.ComplianceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanFacilityRepository loanFacilityRepository;
    private final AccountRepository accountRepository;
    private final ComplianceService complianceService;

    public LoanController(LoanFacilityRepository loanFacilityRepository,
                          AccountRepository accountRepository,
                          ComplianceService complianceService) {
        this.loanFacilityRepository = loanFacilityRepository;
        this.accountRepository = accountRepository;
        this.complianceService = complianceService;
    }

    @GetMapping
    public ResponseEntity<List<LoanFacility>> allLoans() {
        return ResponseEntity.ok(loanFacilityRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<LoanFacility> createLoan(@RequestBody Map<String, Object> payload) {
        Long accountId = Long.valueOf(payload.get("accountId").toString());
        String facilityName = payload.get("facilityName").toString();
        BigDecimal creditLimit = new BigDecimal(payload.get("creditLimit").toString());
        BigDecimal interestRate = new BigDecimal(payload.get("interestRate").toString());
        Integer termMonths = Integer.valueOf(payload.get("termMonths").toString());

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        LoanFacility loan = new LoanFacility(account, facilityName, creditLimit, interestRate, termMonths, LocalDate.now());
        LoanFacility saved = loanFacilityRepository.save(loan);

        complianceService.logAudit("Created Credit Facility: " + facilityName + " for Account " + account.getName() + " with Limit " + creditLimit, "LOW");
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Map<String, Object>> getAmortizationSchedule(@PathVariable Long id) {
        LoanFacility loan = loanFacilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan facility not found"));

        BigDecimal principal = loan.getCreditLimit();
        BigDecimal annualRate = loan.getInterestRate();
        int term = loan.getTermMonths();

        // Monthly rate = Annual rate / 12 / 100
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);

        // Calculate EMI: [P * R * (1+R)^N] / [(1+R)^N - 1]
        BigDecimal emi;
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            emi = principal.divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
            BigDecimal onePlusRPowerN = onePlusR.pow(term);
            BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowerN);
            BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
            emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }

        List<Map<String, Object>> schedule = new ArrayList<>();
        BigDecimal balance = principal;
        BigDecimal totalInterest = BigDecimal.ZERO;

        for (int month = 1; month <= term; month++) {
            BigDecimal interestPayment = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPayment = emi.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);

            if (month == term) {
                // Adjust last payment
                principalPayment = balance;
                emi = principalPayment.add(interestPayment);
                balance = BigDecimal.ZERO;
            } else {
                balance = balance.subtract(principalPayment).setScale(2, RoundingMode.HALF_UP);
            }

            totalInterest = totalInterest.add(interestPayment);

            Map<String, Object> paymentRow = new HashMap<>();
            paymentRow.put("month", month);
            paymentRow.put("emi", emi);
            paymentRow.put("interest", interestPayment);
            paymentRow.put("principal", principalPayment);
            paymentRow.put("remainingBalance", balance);
            schedule.add(paymentRow);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("loanId", loan.getId());
        response.put("facilityName", loan.getFacilityName());
        response.put("creditLimit", loan.getCreditLimit());
        response.put("interestRate", loan.getInterestRate());
        response.put("termMonths", loan.getTermMonths());
        response.put("monthlyEmi", emi);
        response.put("totalInterestPaid", totalInterest);
        response.put("schedule", schedule);

        return ResponseEntity.ok(response);
    }
}
