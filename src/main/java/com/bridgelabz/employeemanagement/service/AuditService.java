package com.bridgelabz.employeemanagement.service;

import com.bridgelabz.employeemanagement.dto.response.AuditLogResponseDTO;
import com.bridgelabz.employeemanagement.entity.User;

import java.util.List;

public interface AuditService {

    void saveAuditLog(
            User user,
            String action,
            String method,
            Long executionTime,
            String status
    );

    List<AuditLogResponseDTO> getAllAuditLogs();
}