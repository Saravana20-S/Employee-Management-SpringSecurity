package com.bridgelabz.employeemanagement.service.impl;

import com.bridgelabz.employeemanagement.dto.response.AuditLogResponseDTO;
import com.bridgelabz.employeemanagement.entity.AuditLog;
import com.bridgelabz.employeemanagement.entity.User;
import com.bridgelabz.employeemanagement.repository.AuditLogRepository;
import com.bridgelabz.employeemanagement.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void saveAuditLog(
            User user,
            String action,
            String method,
            Long executionTime,
            String status) {

        LocalDateTime requestTime =
                LocalDateTime.now();

        AuditLog auditLog =
                AuditLog.builder()
                        .user(user)
                        .action(action)
                        .method(method)
                        .requestTime(requestTime)
                        .responseTime(LocalDateTime.now())
                        .executionTime(executionTime)
                        .status(status)
                        .build();

        auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogResponseDTO> getAllAuditLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(log ->
                        AuditLogResponseDTO.builder()
                                .id(log.getId())
                                .userId(
                                        log.getUser() != null
                                                ? log.getUser().getId()
                                                : null
                                )
                                .userEmail(
                                        log.getUser() != null
                                                ? log.getUser().getEmail()
                                                : null
                                )
                                .action(log.getAction())
                                .method(log.getMethod())
                                .executionTime(
                                        log.getExecutionTime()
                                )
                                .status(log.getStatus())
                                .createdAt(log.getCreatedAt())
                                .build()
                )
                .toList();
    }
}