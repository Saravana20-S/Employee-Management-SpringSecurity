package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.entity.Employee;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.exception.FileProcessingException;
import com.bridgelabz.employeemanagement.model.EmployeeExcelRow;
import com.bridgelabz.employeemanagement.repository.EmployeeRepository;
import com.bridgelabz.employeemanagement.repository.FailedEmployeeRecordRepository;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EmployeeBatchConfig {

    private final EmployeeRepository employeeRepository;
    private final FileUploadRepository fileUploadRepository;
    private final FailedEmployeeRecordRepository failedRecordRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    @StepScope
    public EmployeeExcelReader employeeReader(
            @Value("#{jobParameters['uploadId']}") Long uploadId) {

        FileUpload upload = fileUploadRepository.findById(uploadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Upload not found: " + uploadId
                        )
                );

        return new EmployeeExcelReader(
                upload.getFilePath()
        );
    }


    @Bean
    @StepScope
    public ItemProcessor<EmployeeExcelRow, Employee> employeeProcessor(
            @Value("#{jobParameters['uploadId']}") Long uploadId) {

        FileUpload upload = fileUploadRepository.findById(uploadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Upload not found: " + uploadId
                        )
                );

        return new EmployeeProcessor(
                employeeRepository,
                upload
        );
    }


    @Bean
    @StepScope
    public ItemWriter<Employee> employeeWriter(
            @Value("#{jobParameters['uploadId']}") Long uploadId) {

        FileUpload upload = fileUploadRepository.findById(uploadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Upload not found: " + uploadId
                        )
                );

        return new EmployeeWriter(
                employeeRepository,
                fileUploadRepository,
                upload
        );
    }


    @Bean
    @StepScope
    public BatchFailureListener batchFailureListener(
            @Value("#{jobParameters['uploadId']}") Long uploadId) {

        FileUpload upload = fileUploadRepository.findById(uploadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Upload not found: " + uploadId
                        )
                );

        return new BatchFailureListener(
                failedRecordRepository,
                fileUploadRepository,
                upload
        );
    }


    @Bean
    public Step employeeImportStep(
            EmployeeExcelReader employeeReader,
            ItemProcessor<EmployeeExcelRow, Employee> employeeProcessor,
            ItemWriter<Employee> employeeWriter,
            BatchFailureListener batchFailureListener) {

        return new StepBuilder(
                "employeeImportStep",
                jobRepository
        )
                .<EmployeeExcelRow, Employee>chunk(10)

                .transactionManager(transactionManager)

                .reader(employeeReader)

                .processor(employeeProcessor)

                .writer(employeeWriter)

                .faultTolerant()

                .skip(FileProcessingException.class)

                .skipLimit(100)

                .listener(batchFailureListener)

                .build();
    }


    @Bean
    public Job employeeImportJob(
            Step employeeImportStep) {

        return new JobBuilder(
                "employeeImportJob",
                jobRepository
        )
                .listener(
                        new BatchJobListener(
                                fileUploadRepository
                        )
                )
                .start(employeeImportStep)
                .build();
    }
}