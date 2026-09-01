package com.bridgelabz.employeemanagement.dto.response;

import com.bridgelabz.employeemanagement.enums.BatchStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponseDTO {

    private Long id;

    private String fileName;

    private Integer totalRecords;

    private Integer successRecords;

    private Integer failedRecords;

    private BatchStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}