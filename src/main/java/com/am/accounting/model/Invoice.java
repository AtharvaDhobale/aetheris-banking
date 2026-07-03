package com.am.accounting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String referenceNumber;

    @NotBlank
    @Column(nullable = false)
    private String customerName;

    @NotNull
    @Column(nullable = false)
    private LocalDate invoiceDate;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status = "DRAFT";

    public Invoice() {
    }

    public Invoice(String referenceNumber, String customerName, LocalDate invoiceDate, BigDecimal amount) {
        this.referenceNumber = referenceNumber;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
