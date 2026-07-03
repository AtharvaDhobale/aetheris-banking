package com.am.accounting.service;

import com.am.accounting.model.KnowledgeTopic;
import com.am.accounting.repository.KnowledgeTopicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final KnowledgeTopicRepository knowledgeTopicRepository;
    private final List<String> operations = List.of(
            "Create Ledger Account",
            "Create Invoice",
            "Record Debit/Credit Transaction",
            "Bank Reconciliation",
            "Import Excel Data",
            "Create Tender Request",
            "View Financial Statements",
            "GST Calculation",
            "GST Return Filing",
            "Depreciation Schedule",
            "Tax Return Filing",
            "Payroll Voucher",
            "Client Ledger Report",
            "Vendor Payment",
            "Journal Entry",
            "Employee Payroll",
            "Expense Voucher",
            "Receipt Voucher",
            "Payment Voucher",
            "Debit Note",
            "Credit Note",
            "Purchase Ledger",
            "Sales Ledger",
            "Trial Balance",
            "Balance Sheet",
            "Profit & Loss Statement",
            "Cash Flow Statement",
            "Employee History Lookup",
            "Activity Report",
            "Tax Compliance Check",
            "Audit Trail Report"
    );

    public SearchService(KnowledgeTopicRepository knowledgeTopicRepository) {
        this.knowledgeTopicRepository = knowledgeTopicRepository;
    }

    public List<String> suggestOperations(String query) {
        if (query == null || query.isBlank()) {
            return operations;
        }
        String lower = query.toLowerCase();
        return operations.stream()
                .filter(operation -> operation.toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<KnowledgeTopic> searchKnowledge(String query) {
        if (query == null || query.isBlank()) {
            return knowledgeTopicRepository.findAll();
        }
        return knowledgeTopicRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query);
    }
}
