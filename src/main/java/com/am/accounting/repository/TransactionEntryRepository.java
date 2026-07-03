package com.am.accounting.repository;

import com.am.accounting.model.TransactionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, Long> {
    List<TransactionEntry> findByAccountId(Long accountId);
}
