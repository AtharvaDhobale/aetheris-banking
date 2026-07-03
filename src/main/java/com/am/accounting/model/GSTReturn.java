package com.am.accounting.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gst_returns")
public class GSTReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String returnPeriod;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private BigDecimal totalInwardSupply = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalOutwardSupply = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal inputTaxCredit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal outputTax = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal netGSTPayable;

    @Column(nullable = false)
    private String status = "DRAFT";

    public GSTReturn() {
    }

    public GSTReturn(String returnPeriod, LocalDate submissionDate, BigDecimal totalInwardSupply,
                     BigDecimal totalOutwardSupply, BigDecimal inputTaxCredit, BigDecimal outputTax) {
        this.returnPeriod = returnPeriod;
        this.submissionDate = submissionDate;
        this.totalInwardSupply = totalInwardSupply;
        this.totalOutwardSupply = totalOutwardSupply;
        this.inputTaxCredit = inputTaxCredit;
        this.outputTax = outputTax;
        this.netGSTPayable = outputTax.subtract(inputTaxCredit);
    }

    public Long getId() {
        return id;
    }

    public String getReturnPeriod() {
        return returnPeriod;
    }

    public void setReturnPeriod(String returnPeriod) {
        this.returnPeriod = returnPeriod;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public BigDecimal getTotalInwardSupply() {
        return totalInwardSupply;
    }

    public void setTotalInwardSupply(BigDecimal totalInwardSupply) {
        this.totalInwardSupply = totalInwardSupply;
    }

    public BigDecimal getTotalOutwardSupply() {
        return totalOutwardSupply;
    }

    public void setTotalOutwardSupply(BigDecimal totalOutwardSupply) {
        this.totalOutwardSupply = totalOutwardSupply;
    }

    public BigDecimal getInputTaxCredit() {
        return inputTaxCredit;
    }

    public void setInputTaxCredit(BigDecimal inputTaxCredit) {
        this.inputTaxCredit = inputTaxCredit;
    }

    public BigDecimal getOutputTax() {
        return outputTax;
    }

    public void setOutputTax(BigDecimal outputTax) {
        this.outputTax = outputTax;
    }

    public BigDecimal getNetGSTPayable() {
        return netGSTPayable;
    }

    public void setNetGSTPayable(BigDecimal netGSTPayable) {
        this.netGSTPayable = netGSTPayable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
