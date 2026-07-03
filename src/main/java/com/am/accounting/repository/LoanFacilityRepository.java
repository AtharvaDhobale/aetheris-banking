package com.am.accounting.repository;

import com.am.accounting.model.LoanFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanFacilityRepository extends JpaRepository<LoanFacility, Long> {
}
