package com.am.accounting.controller;

import com.am.accounting.model.AuditLog;
import com.am.accounting.service.ComplianceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
public class AuditLogController {
    private final ComplianceService complianceService;

    public AuditLogController(ComplianceService complianceService) {
        this.complianceService = complianceService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(complianceService.getLogs());
    }
}
