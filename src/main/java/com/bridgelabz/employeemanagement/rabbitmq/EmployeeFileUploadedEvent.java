package com.bridgelabz.employeemanagement.rabbitmq;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFileUploadedEvent {

    private Long uploadId;

    private String fileName;

    private String uploadedBy;

    private String eventType;
}