package com.am.accounting.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tax_returns")
public class TaxReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String assessmentYear;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal taxableIncome = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal taxPayable = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal rebates = BigDecimal.ZERO;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String status = "DRAFT";

    public TaxReturn() {
    }

    public TaxReturn(String assessmentYear, LocalDate dueDate, BigDecimal totalIncome, String panNumber) {
        this.assessmentYear = assessmentYear;
        this.dueDate = dueDate;
        this.totalIncome = totalIncome;
        this.panNumber = panNumber;
    }

    public Long getId() {
        return id;
    }

    public String getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(String assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public BigDecimal getTaxPayable() {
        return taxPayable;
    }

    public void setTaxPayable(BigDecimal taxPayable) {
        this.taxPayable = taxPayable;
    }

    public BigDecimal getRebates() {
        return rebates;
    }

    public void setRebates(BigDecimal rebates) {
        this.rebates = rebates;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
