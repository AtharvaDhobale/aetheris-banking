package com.am.accounting.controller;

import com.am.accounting.model.TaxReturn;
import com.am.accounting.repository.TaxReturnRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tax-returns")
public class TaxReturnController {
    private final TaxReturnRepository taxReturnRepository;

    public TaxReturnController(TaxReturnRepository taxReturnRepository) {
        this.taxReturnRepository = taxReturnRepository;
    }

    @GetMapping
    public ResponseEntity<List<TaxReturn>> allReturns() {
        return ResponseEntity.ok(taxReturnRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TaxReturn> createReturn(@RequestBody TaxReturn taxReturn) {
        return ResponseEntity.ok(taxReturnRepository.save(taxReturn));
    }
}
