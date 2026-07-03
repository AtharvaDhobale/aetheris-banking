package com.am.accounting.controller;

import com.am.accounting.model.KnowledgeTopic;
import com.am.accounting.repository.KnowledgeTopicRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final KnowledgeTopicRepository knowledgeTopicRepository;

    public KnowledgeController(KnowledgeTopicRepository knowledgeTopicRepository) {
        this.knowledgeTopicRepository = knowledgeTopicRepository;
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeTopic>> allTopics() {
        return ResponseEntity.ok(knowledgeTopicRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeTopic> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeTopicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found")));
    }
}
