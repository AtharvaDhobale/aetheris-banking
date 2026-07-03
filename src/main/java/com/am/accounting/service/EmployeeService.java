package com.am.accounting.service;

import com.am.accounting.model.Employee;
import com.am.accounting.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> suggestEmployees(String query) {
        if (query == null || query.isBlank()) {
            return employeeRepository.findAll();
        }
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> results = employeeRepository.findByNameContainingIgnoreCase(query, pageable);
        return results.getContent();
    }

    public Employee getEmployeeDetails(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    public List<String> getEmployeeHistory(Long employeeId) {
        Employee emp = getEmployeeDetails(employeeId);
        return List.of(
                "Account Number: " + emp.getBankAccountNumber(),
                "Bank Name: " + emp.getBankName(),
                "IFSC: " + emp.getIfscCode(),
                "PAN: " + emp.getPanNumber(),
                "Current Salary: " + emp.getSalary(),
                "Department: " + emp.getDepartment()
        );
    }
}
