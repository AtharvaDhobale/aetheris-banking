package com.am.accounting.controller;

import com.am.accounting.model.DepreciationSchedule;
import com.am.accounting.repository.DepreciationScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/depreciation")
public class DepreciationController {
    private final DepreciationScheduleRepository depreciationRepository;

    public DepreciationController(DepreciationScheduleRepository depreciationRepository) {
        this.depreciationRepository = depreciationRepository;
    }

    @GetMapping
    public ResponseEntity<List<DepreciationSchedule>> allSchedules() {
        return ResponseEntity.ok(depreciationRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<DepreciationSchedule> createSchedule(@RequestBody DepreciationSchedule schedule) {
        return ResponseEntity.ok(depreciationRepository.save(schedule));
    }
}
