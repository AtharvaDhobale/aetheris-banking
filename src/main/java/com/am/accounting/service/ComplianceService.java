package com.am.accounting.service;

import com.am.accounting.model.AuditLog;
import com.am.accounting.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplianceService {
    private final AuditLogRepository auditLogRepository;

    public ComplianceService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAudit(String action, String riskLevel) {
        String username = "ANONYMOUS";
        String role = "NONE";

        Object principal = SecurityContextHolder.getContext().getAuthentication() != null 
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() 
                : null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            role = ((UserDetails) principal).getAuthorities().toString();
        } else if (principal != null) {
            username = principal.toString();
        }

        String ipAddress = "0.0.0.0";
        String userAgent = "Unknown";

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            ipAddress = request.getRemoteAddr();
            userAgent = request.getHeader("User-Agent");
            if (userAgent == null) userAgent = "Direct API Call";
        }

        AuditLog log = new AuditLog(username, role, action, ipAddress, userAgent, riskLevel);
        auditLogRepository.save(log);
    }

    public boolean isAMLSuspicious(BigDecimal amount) {
        // High transaction threshold is 500,000 (INR/USD equivalent)
        BigDecimal threshold = new BigDecimal("500000");
        return amount.compareTo(threshold) >= 0;
    }

    public List<AuditLog> getLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}
