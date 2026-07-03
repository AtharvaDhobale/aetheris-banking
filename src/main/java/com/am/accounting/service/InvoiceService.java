package com.am.accounting.service;

import com.am.accounting.model.Invoice;
import com.am.accounting.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public Invoice create(Invoice invoice) {
        invoice.setStatus(invoice.getStatus() == null ? "DRAFT" : invoice.getStatus());
        return invoiceRepository.save(invoice);
    }
}
