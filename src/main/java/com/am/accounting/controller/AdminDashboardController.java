package com.am.accounting.controller;

import com.am.accounting.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayDashboard() {
        return ResponseEntity.ok(adminDashboardService.getTodayDashboard());
    }

    @GetMapping("/employee-reports")
    public ResponseEntity<List<Map<String, Object>>> getEmployeeReports() {
        return ResponseEntity.ok(adminDashboardService.getEmployeeReports());
    }

    @GetMapping("/payroll-summary")
    public ResponseEntity<Map<String, Object>> getPayrollSummary() {
        return ResponseEntity.ok(adminDashboardService.getPayrollSummary());
    }
}
