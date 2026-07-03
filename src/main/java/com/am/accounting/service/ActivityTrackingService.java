package com.am.accounting.service;

import com.am.accounting.model.EmployeeActivity;
import com.am.accounting.model.Employee;
import com.am.accounting.repository.EmployeeActivityRepository;
import com.am.accounting.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityTrackingService {
    private final EmployeeActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;

    public ActivityTrackingService(EmployeeActivityRepository activityRepository,
                                   EmployeeRepository employeeRepository) {
        this.activityRepository = activityRepository;
        this.employeeRepository = employeeRepository;
    }

    public void logActivity(Long employeeId, String activityType, String description) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        EmployeeActivity activity = new EmployeeActivity(employee, activityType, description, LocalDate.now());
        activityRepository.save(activity);
    }

    public List<EmployeeActivity> getTodayActivities() {
        return activityRepository.findByActivityDateOrderByTimestampDesc(LocalDate.now());
    }

    public List<EmployeeActivity> getEmployeeTodayActivities(Long employeeId) {
        return activityRepository.findByEmployeeIdAndActivityDate(employeeId, LocalDate.now());
    }

    public Map<String, Object> getEmployeeDailySummary(Long employeeId) {
        List<EmployeeActivity> activities = getEmployeeTodayActivities(employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("employeeName", employee.getName());
        summary.put("employeeId", employee.getEmployeeId());
        summary.put("activitiesCount", activities.size());
        summary.put("activities", activities);
        return summary;
    }
}
