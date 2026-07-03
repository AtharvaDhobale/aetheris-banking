package com.am.accounting.repository;

import com.am.accounting.model.KnowledgeTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeTopicRepository extends JpaRepository<KnowledgeTopic, Long> {
    List<KnowledgeTopic> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);
}
