package com.am.accounting.service;

import com.am.accounting.model.EmployeeActivity;
import com.am.accounting.model.PayrollEntry;
import com.am.accounting.repository.EmployeeActivityRepository;
import com.am.accounting.repository.EmployeeRepository;
import com.am.accounting.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {
    private final EmployeeActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;

    public AdminDashboardService(EmployeeActivityRepository activityRepository,
                                 EmployeeRepository employeeRepository,
                                 PayrollRepository payrollRepository) {
        this.activityRepository = activityRepository;
        this.employeeRepository = employeeRepository;
        this.payrollRepository = payrollRepository;
    }

    public Map<String, Object> getTodayDashboard() {
        List<EmployeeActivity> todayActivities = activityRepository.findByActivityDateOrderByTimestampDesc(LocalDate.now());
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalActivities", todayActivities.size());
        dashboard.put("totalEmployees", employeeRepository.count());
        
        Map<String, Long> activitiesByType = todayActivities.stream()
                .collect(Collectors.groupingBy(EmployeeActivity::getActivityType, Collectors.counting()));
        dashboard.put("activitiesByType", activitiesByType);
        
        Map<String, List<EmployeeActivity>> activitiesByEmployee = todayActivities.stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getName()));
        dashboard.put("activitiesByEmployee", activitiesByEmployee);
        
        return dashboard;
    }

    public List<Map<String, Object>> getEmployeeReports() {
        return employeeRepository.findAll().stream()
                .map(emp -> {
                    Map<String, Object> report = new HashMap<>();
                    report.put("employeeName", emp.getName());
                    report.put("employeeId", emp.getEmployeeId());
                    report.put("designation", emp.getDesignation());
                    report.put("department", emp.getDepartment());
                    
                    List<EmployeeActivity> todayActivities = activityRepository
                            .findByEmployeeIdAndActivityDate(emp.getId(), LocalDate.now());
                    report.put("todayActivitiesCount", todayActivities.size());
                    
                    List<PayrollEntry> payrollEntries = payrollRepository.findByEmployeeId(emp.getId());
                    if (!payrollEntries.isEmpty()) {
                        report.put("lastSalary", payrollEntries.get(payrollEntries.size() - 1).getNetSalary());
                    }
                    
                    return report;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPayrollSummary() {
        List<PayrollEntry> todayPayrolls = payrollRepository.findAll().stream()
                .filter(p -> p.getPaymentDate().equals(LocalDate.now()))
                .collect(Collectors.toList());
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("payrollCount", todayPayrolls.size());
        summary.put("totalPayroll", todayPayrolls.stream()
                .map(PayrollEntry::getNetSalary)
                .reduce((a, b) -> a.add(b))
                .orElse(null));
        
        return summary;
    }
}
