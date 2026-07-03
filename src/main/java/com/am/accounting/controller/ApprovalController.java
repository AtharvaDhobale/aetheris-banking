package com.am.accounting.controller;

import com.am.accounting.model.Invoice;
import com.am.accounting.model.JournalEntry;
import com.am.accounting.model.VendorPayment;
import com.am.accounting.repository.InvoiceRepository;
import com.am.accounting.repository.JournalEntryRepository;
import com.am.accounting.repository.VendorPaymentRepository;
import com.am.accounting.service.ComplianceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {
    private final InvoiceRepository invoiceRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final VendorPaymentRepository vendorPaymentRepository;
    private final ComplianceService complianceService;

    public ApprovalController(InvoiceRepository invoiceRepository,
                              JournalEntryRepository journalEntryRepository,
                              VendorPaymentRepository vendorPaymentRepository,
                              ComplianceService complianceService) {
        this.invoiceRepository = invoiceRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.vendorPaymentRepository = vendorPaymentRepository;
        this.complianceService = complianceService;
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingApprovals() {
        List<Invoice> pendingInvoices = invoiceRepository.findAll().stream()
                .filter(i -> "PENDING_APPROVAL".equalsIgnoreCase(i.getStatus()) || "DRAFT".equalsIgnoreCase(i.getStatus()) || "FLAGGED_HIGH_RISK".equalsIgnoreCase(i.getStatus()))
                .collect(Collectors.toList());

        List<JournalEntry> pendingJournalEntries = journalEntryRepository.findAll().stream()
                .filter(j -> "PENDING_APPROVAL".equalsIgnoreCase(j.getStatus()))
                .collect(Collectors.toList());

        List<VendorPayment> pendingVendorPayments = vendorPaymentRepository.findAll().stream()
                .filter(v -> "PENDING".equalsIgnoreCase(v.getStatus()) || "PENDING_APPROVAL".equalsIgnoreCase(v.getStatus()) || "FLAGGED_HIGH_RISK".equalsIgnoreCase(v.getStatus()))
                .collect(Collectors.toList());

        Map<String, Object> pendingMap = new HashMap<>();
        pendingMap.put("invoices", pendingInvoices);
        pendingMap.put("journalEntries", pendingJournalEntries);
        pendingMap.put("vendorPayments", pendingVendorPayments);

        return ResponseEntity.ok(pendingMap);
    }

    @PostMapping("/invoices/{id}/approve")
    public ResponseEntity<Invoice> approveInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus("APPROVED");
        Invoice saved = invoiceRepository.save(invoice);
        complianceService.logAudit("Authorized Invoice: " + invoice.getReferenceNumber() + " for amount " + invoice.getAmount(), "LOW");
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/invoices/{id}/reject")
    public ResponseEntity<Invoice> rejectInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus("REJECTED");
        Invoice saved = invoiceRepository.save(invoice);
        complianceService.logAudit("Rejected Invoice: " + invoice.getReferenceNumber(), "LOW");
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/journal-entries/{id}/approve")
    public ResponseEntity<JournalEntry> approveJournalEntry(@PathVariable Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Journal Entry not found"));
        entry.setStatus("APPROVED");
        JournalEntry saved = journalEntryRepository.save(entry);
        complianceService.logAudit("Authorized Journal Entry Voucher: " + entry.getVoucherNumber() + " of amount " + entry.getAmount(), "LOW");
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/journal-entries/{id}/reject")
    public ResponseEntity<JournalEntry> rejectJournalEntry(@PathVariable Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Journal Entry not found"));
        entry.setStatus("REJECTED");
        JournalEntry saved = journalEntryRepository.save(entry);
        complianceService.logAudit("Rejected Journal Entry Voucher: " + entry.getVoucherNumber(), "LOW");
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/vendor-payments/{id}/approve")
    public ResponseEntity<VendorPayment> approveVendorPayment(@PathVariable Long id) {
        VendorPayment payment = vendorPaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor Payment not found"));
        payment.setStatus("APPROVED");
        VendorPayment saved = vendorPaymentRepository.save(payment);
        complianceService.logAudit("Authorized Vendor Payment to: " + payment.getVendorName() + " for total amount " + payment.getTotalAmount(), "LOW");
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/vendor-payments/{id}/reject")
    public ResponseEntity<VendorPayment> rejectVendorPayment(@PathVariable Long id) {
        VendorPayment payment = vendorPaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor Payment not found"));
        payment.setStatus("REJECTED");
        VendorPayment saved = vendorPaymentRepository.save(payment);
        complianceService.logAudit("Rejected Vendor Payment to: " + payment.getVendorName(), "LOW");
        return ResponseEntity.ok(saved);
    }
}
