package com.am.accounting.repository;

import com.am.accounting.model.TenderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderRequestRepository extends JpaRepository<TenderRequest, Long> {
}
