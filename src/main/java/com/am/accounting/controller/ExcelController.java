package com.am.accounting.controller;

import com.am.accounting.model.Client;
import com.am.accounting.service.ExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/import/clients")
    public ResponseEntity<List<Client>> importClients(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(excelService.importClients(file));
    }
}
