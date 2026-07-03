package com.am.accounting.controller;

import com.am.accounting.model.Account;
import com.am.accounting.model.TransactionEntry;
import com.am.accounting.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> allAccounts() {
        return accountService.findAll();
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.ok(accountService.create(account));
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionEntry> addTransaction(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionEntry transactionEntry) {
        transactionEntry.setAccount(new Account());
        transactionEntry.getAccount().setId(accountId);
        return ResponseEntity.ok(accountService.addTransaction(transactionEntry));
    }
}
