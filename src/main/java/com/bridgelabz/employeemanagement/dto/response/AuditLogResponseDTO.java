package com.bridgelabz.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {

    private Long id;
    private Long userId;
    private String userEmail;
    private String action;
    private String method;
    private Long executionTime;
    private String status;
    private LocalDateTime createdAt;
}