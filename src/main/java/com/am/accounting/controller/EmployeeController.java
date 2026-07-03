package com.am.accounting.controller;

import com.am.accounting.model.Employee;
import com.am.accounting.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<Map<String, Object>>> suggestEmployees(@RequestParam(required = false) String query) {
        List<Employee> employees = employeeService.suggestEmployees(query);
        List<Map<String, Object>> suggestions = employees.stream()
                .map(emp -> {
                    Map<String, Object> suggestion = new HashMap<>();
                    suggestion.put("id", emp.getId());
                    suggestion.put("name", emp.getName());
                    suggestion.put("employeeId", emp.getEmployeeId());
                    suggestion.put("designation", emp.getDesignation());
                    return suggestion;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Map<String, Object>> getEmployeeHistory(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeDetails(id);
        List<String> history = employeeService.getEmployeeHistory(id);
        Map<String, Object> response = new HashMap<>();
        response.put("employeeName", employee.getName());
        response.put("employeeId", employee.getEmployeeId());
        response.put("bankDetails", history);
        return ResponseEntity.ok(response);
    }
}
