package com.am.accounting.repository;

import com.am.accounting.model.PayrollEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntry, Long> {
    List<PayrollEntry> findByEmployeeId(Long employeeId);
}
