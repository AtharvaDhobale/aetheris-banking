package com.am.accounting.controller;

import com.am.accounting.model.TransactionEntry;
import com.am.accounting.service.BankReconciliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation")
public class BankReconciliationController {
    private final BankReconciliationService bankReconciliationService;

    public BankReconciliationController(BankReconciliationService bankReconciliationService) {
        this.bankReconciliationService = bankReconciliationService;
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Map<String, Object>> reconcileAccount(@PathVariable Long accountId) {
        BigDecimal balance = bankReconciliationService.calculateOutstandingBalance(accountId);
        List<TransactionEntry> transactions = bankReconciliationService.getTransactionsForAccount(accountId);

        return ResponseEntity.ok(Map.of(
                "accountId", accountId,
                "balance", balance,
                "transactions", transactions
        ));
    }
}
