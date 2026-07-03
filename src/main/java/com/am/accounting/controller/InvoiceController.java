package com.am.accounting.controller;

import com.am.accounting.model.Invoice;
import com.am.accounting.service.InvoiceService;
import com.am.accounting.service.ComplianceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final ComplianceService complianceService;

    public InvoiceController(InvoiceService invoiceService, ComplianceService complianceService) {
        this.invoiceService = invoiceService;
        this.complianceService = complianceService;
    }

    @GetMapping
    public List<Invoice> allInvoices() {
        return invoiceService.findAll();
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        boolean amlSuspicious = complianceService.isAMLSuspicious(invoice.getAmount());
        if (amlSuspicious) {
            invoice.setStatus("FLAGGED_HIGH_RISK");
            complianceService.logAudit("SUSPICIOUS: Submitted high-value Invoice " + invoice.getReferenceNumber() + " of amount " + invoice.getAmount() + " (Requires special clearance)", "HIGH");
        } else {
            invoice.setStatus("PENDING_APPROVAL");
            complianceService.logAudit("Submitted Invoice: " + invoice.getReferenceNumber() + " for amount " + invoice.getAmount() + " (Pending Authorization)", "LOW");
        }
        return ResponseEntity.ok(invoiceService.create(invoice));
    }
}
