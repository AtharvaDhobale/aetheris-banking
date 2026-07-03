package com.am.accounting.repository;

import com.am.accounting.model.EmployeeActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeActivityRepository extends JpaRepository<EmployeeActivity, Long> {
    List<EmployeeActivity> findByEmployeeIdAndActivityDate(Long employeeId, LocalDate activityDate);
    List<EmployeeActivity> findByActivityDateOrderByTimestampDesc(LocalDate activityDate);
}
