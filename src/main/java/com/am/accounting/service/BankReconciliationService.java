package com.am.accounting.service;

import com.am.accounting.model.Account;
import com.am.accounting.model.TransactionEntry;
import com.am.accounting.repository.AccountRepository;
import com.am.accounting.repository.TransactionEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankReconciliationService {
    private final AccountRepository accountRepository;
    private final TransactionEntryRepository transactionEntryRepository;

    public BankReconciliationService(AccountRepository accountRepository, TransactionEntryRepository transactionEntryRepository) {
        this.accountRepository = accountRepository;
        this.transactionEntryRepository = transactionEntryRepository;
    }

    public Account getAccountBalance(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public List<TransactionEntry> getTransactionsForAccount(Long accountId) {
        return transactionEntryRepository.findByAccountId(accountId);
    }

    public BigDecimal calculateOutstandingBalance(Long accountId) {
        Account account = getAccountBalance(accountId);
        return account.getBalance();
    }
}
