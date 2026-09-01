package com.bridgelabz.employeemanagement.service.impl;

import com.bridgelabz.employeemanagement.dto.response.BatchStatusResponseDTO;
import com.bridgelabz.employeemanagement.dto.response.FailedRecordResponseDTO;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.exception.ResourceNotFoundException;
import com.bridgelabz.employeemanagement.repository.FailedEmployeeRecordRepository;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import com.bridgelabz.employeemanagement.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final JobLauncher jobLauncher;
    private final Job employeeImportJob;

    private final FileUploadRepository fileUploadRepository;
    private final FailedEmployeeRecordRepository
            failedEmployeeRecordRepository;

    @Override
    public void startBatchJob(Long uploadId) {

        try {

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addLong(
                                    "uploadId",
                                    uploadId
                            )
                            .addLong(
                                    "time",
                                    System.currentTimeMillis()
                            )
                            .toJobParameters();

            jobLauncher.run(
                    employeeImportJob,
                    parameters
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to start batch job",
                    exception
            );
        }
    }

    @Override
    public BatchStatusResponseDTO getBatchStatus(
            Long uploadId) {

        FileUpload upload =
                getUpload(uploadId);

        return BatchStatusResponseDTO.builder()
                .uploadId(upload.getId())
                .fileName(upload.getFileName())
                .status(upload.getStatus())
                .totalRecords(upload.getTotalRecords())
                .successRecords(upload.getSuccessRecords())
                .failedRecords(upload.getFailedRecords())
                .build();
    }

    @Override
    public List<FailedRecordResponseDTO>
    getFailedRecords(Long uploadId) {

        FileUpload upload =
                getUpload(uploadId);

        return failedEmployeeRecordRepository
                .findByUpload(upload)
                .stream()
                .map(record ->
                        FailedRecordResponseDTO.builder()
                                .id(record.getId())
                                .rowNumber(
                                        record.getRowNumber()
                                )
                                .employeeData(
                                        record.getEmployeeData()
                                )
                                .errorMessage(
                                        record.getErrorMessage()
                                )
                                .build()
                )
                .toList();
    }

    private FileUpload getUpload(Long uploadId) {

        return fileUploadRepository
                .findById(uploadId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Upload not found: " + uploadId
                        )
                );
    }
}