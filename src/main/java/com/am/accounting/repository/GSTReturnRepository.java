package com.am.accounting.repository;

import com.am.accounting.model.GSTReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTReturnRepository extends JpaRepository<GSTReturn, Long> {
}
