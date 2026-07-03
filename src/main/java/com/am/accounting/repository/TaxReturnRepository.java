package com.am.accounting.repository;

import com.am.accounting.model.TaxReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxReturnRepository extends JpaRepository<TaxReturn, Long> {
}
