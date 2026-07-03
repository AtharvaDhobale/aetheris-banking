package com.am.accounting.controller;

import com.am.accounting.model.PayrollEntry;
import com.am.accounting.repository.PayrollRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    private final PayrollRepository payrollRepository;

    public PayrollController(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    @GetMapping
    public ResponseEntity<List<PayrollEntry>> allPayroll() {
        return ResponseEntity.ok(payrollRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<PayrollEntry> createPayroll(@RequestBody PayrollEntry payrollEntry) {
        return ResponseEntity.ok(payrollRepository.save(payrollEntry));
    }
}
