package com.am.accounting.controller;

import com.am.accounting.model.JournalEntry;
import com.am.accounting.repository.JournalEntryRepository;
import com.am.accounting.service.ComplianceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/journal-entries")
public class JournalEntryController {
    private final JournalEntryRepository journalEntryRepository;
    private final ComplianceService complianceService;

    public JournalEntryController(JournalEntryRepository journalEntryRepository, ComplianceService complianceService) {
        this.journalEntryRepository = journalEntryRepository;
        this.complianceService = complianceService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> allEntries() {
        return ResponseEntity.ok(journalEntryRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry) {
        journalEntry.setStatus("PENDING_APPROVAL");
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        complianceService.logAudit("Submitted Journal Entry: " + journalEntry.getVoucherNumber() + " for amount " + journalEntry.getAmount() + " (Pending Authorization)", "LOW");
        return ResponseEntity.ok(saved);
    }
}
