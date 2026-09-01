package com.bridgelabz.employeemanagement.service;

import com.bridgelabz.employeemanagement.dto.response.BatchStatusResponseDTO;
import com.bridgelabz.employeemanagement.dto.response.FailedRecordResponseDTO;

import java.util.List;

public interface BatchService {

    void startBatchJob(Long uploadId);

    BatchStatusResponseDTO getBatchStatus(Long uploadId);

    List<FailedRecordResponseDTO> getFailedRecords(Long uploadId);
}