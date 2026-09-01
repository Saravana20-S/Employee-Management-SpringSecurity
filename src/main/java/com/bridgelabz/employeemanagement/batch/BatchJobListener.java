package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.enums.BatchStatus;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class BatchJobListener
        implements JobExecutionListener {

    private final FileUploadRepository
            fileUploadRepository;

    @Override
    public void beforeJob(
            JobExecution jobExecution) {

        Long uploadId =
                jobExecution
                        .getJobParameters()
                        .getLong("uploadId");

        FileUpload upload =
                fileUploadRepository
                        .findById(uploadId)
                        .orElseThrow();

        upload.setStatus(
                BatchStatus.PROCESSING
        );

        fileUploadRepository.save(upload);
    }

    @Override
    public void afterJob(
            JobExecution jobExecution) {

        Long uploadId =
                jobExecution
                        .getJobParameters()
                        .getLong("uploadId");

        FileUpload upload =
                fileUploadRepository
                        .findById(uploadId)
                        .orElseThrow();

        upload.setCompletedAt(
                LocalDateTime.now()
        );

        if (jobExecution
                .getStatus()
                .isUnsuccessful()) {

            upload.setStatus(
                    BatchStatus.FAILED
            );

        } else if (
                upload.getFailedRecords() > 0) {

            upload.setStatus(
                    BatchStatus.COMPLETED_WITH_ERRORS
            );

        } else {

            upload.setStatus(
                    BatchStatus.COMPLETED
            );
        }

        fileUploadRepository.save(upload);
    }
}