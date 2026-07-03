package com.am.accounting.repository;

import com.am.accounting.model.DepreciationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepreciationScheduleRepository extends JpaRepository<DepreciationSchedule, Long> {
}
