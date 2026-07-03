package com.am.accounting.controller;

import com.am.accounting.model.GSTReturn;
import com.am.accounting.repository.GSTReturnRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gst-returns")
public class GSTReturnController {
    private final GSTReturnRepository gstReturnRepository;

    public GSTReturnController(GSTReturnRepository gstReturnRepository) {
        this.gstReturnRepository = gstReturnRepository;
    }

    @GetMapping
    public ResponseEntity<List<GSTReturn>> allReturns() {
        return ResponseEntity.ok(gstReturnRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<GSTReturn> createReturn(@RequestBody GSTReturn gstReturn) {
        return ResponseEntity.ok(gstReturnRepository.save(gstReturn));
    }
}
