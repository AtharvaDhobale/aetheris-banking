package com.am.accounting.controller;

import com.am.accounting.model.TenderRequest;
import com.am.accounting.repository.TenderRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {
    private final TenderRequestRepository tenderRequestRepository;

    public TenderController(TenderRequestRepository tenderRequestRepository) {
        this.tenderRequestRepository = tenderRequestRepository;
    }

    @GetMapping
    public ResponseEntity<List<TenderRequest>> allTenders() {
        return ResponseEntity.ok(tenderRequestRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TenderRequest> createTender(@RequestBody TenderRequest tenderRequest) {
        return ResponseEntity.ok(tenderRequestRepository.save(tenderRequest));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<TenderRequest> closeTender(@PathVariable Long id) {
        TenderRequest tender = tenderRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tender not found"));
        tender.setStatus("CLOSED");
        return ResponseEntity.ok(tenderRequestRepository.save(tender));
    }
}
