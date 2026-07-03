package com.am.accounting.controller;

import com.am.accounting.model.KnowledgeTopic;
import com.am.accounting.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> suggestOperations(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(searchService.suggestOperations(query));
    }

    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeTopic>> searchKnowledge(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(searchService.searchKnowledge(query));
    }
}
