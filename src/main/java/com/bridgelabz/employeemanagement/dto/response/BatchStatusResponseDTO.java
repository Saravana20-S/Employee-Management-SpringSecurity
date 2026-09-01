package com.bridgelabz.employeemanagement.dto.response;

import com.bridgelabz.employeemanagement.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchStatusResponseDTO {

    private Long uploadId;

    private String fileName;

    private BatchStatus status;

    private Integer totalRecords;

    private Integer successRecords;

    private Integer failedRecords;
}