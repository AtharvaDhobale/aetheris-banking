package com.am.accounting.service;

import com.am.accounting.model.Account;
import com.am.accounting.model.TransactionEntry;
import com.am.accounting.repository.AccountRepository;
import com.am.accounting.repository.TransactionEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionEntryRepository transactionEntryRepository;

    public AccountService(AccountRepository accountRepository, TransactionEntryRepository transactionEntryRepository) {
        this.accountRepository = accountRepository;
        this.transactionEntryRepository = transactionEntryRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Transactional
    public Account create(Account account) {
        account.setBalance(account.getBalance() == null ? BigDecimal.ZERO : account.getBalance());
        return accountRepository.save(account);
    }

    @Transactional
    public TransactionEntry addTransaction(TransactionEntry transactionEntry) {
        Account account = accountRepository.findById(transactionEntry.getAccount().getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found for transaction"));

        BigDecimal updatedBalance = account.getBalance().add(transactionEntry.getAmount());
        account.setBalance(updatedBalance);
        accountRepository.save(account);
        return transactionEntryRepository.save(transactionEntry);
    }
}
