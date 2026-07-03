package com.am.accounting.controller;

import com.am.accounting.model.VendorPayment;
import com.am.accounting.repository.VendorPaymentRepository;
import com.am.accounting.service.ComplianceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-payments")
public class VendorPaymentController {
    private final VendorPaymentRepository vendorPaymentRepository;
    private final ComplianceService complianceService;

    public VendorPaymentController(VendorPaymentRepository vendorPaymentRepository, ComplianceService complianceService) {
        this.vendorPaymentRepository = vendorPaymentRepository;
        this.complianceService = complianceService;
    }

    @GetMapping
    public ResponseEntity<List<VendorPayment>> allPayments() {
        return ResponseEntity.ok(vendorPaymentRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<VendorPayment> createPayment(@RequestBody VendorPayment vendorPayment) {
        boolean amlSuspicious = complianceService.isAMLSuspicious(vendorPayment.getTotalAmount());
        if (amlSuspicious) {
            vendorPayment.setStatus("FLAGGED_HIGH_RISK");
            complianceService.logAudit("SUSPICIOUS: Submitted high-value Vendor Payment to: " + vendorPayment.getVendorName() + " of amount " + vendorPayment.getTotalAmount() + " (Requires special clearance)", "HIGH");
        } else {
            vendorPayment.setStatus("PENDING_APPROVAL");
            complianceService.logAudit("Submitted Vendor Payment to: " + vendorPayment.getVendorName() + " of amount " + vendorPayment.getTotalAmount() + " (Pending Authorization)", "LOW");
        }
        return ResponseEntity.ok(vendorPaymentRepository.save(vendorPayment));
    }
}
