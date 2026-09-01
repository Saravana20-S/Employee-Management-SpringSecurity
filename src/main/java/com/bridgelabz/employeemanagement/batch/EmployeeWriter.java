package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.entity.Employee;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.repository.EmployeeRepository;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;


public class EmployeeWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;
    private final FileUploadRepository fileUploadRepository;
    private final FileUpload upload;

    public EmployeeWriter(
            EmployeeRepository employeeRepository,
            FileUploadRepository fileUploadRepository,
            FileUpload upload) {

        this.employeeRepository = employeeRepository;
        this.fileUploadRepository = fileUploadRepository;
        this.upload = upload;
    }

    @Override
    public void write(Chunk<? extends Employee> chunk) {

        employeeRepository.saveAll(chunk.getItems());

        int currentSuccessRecords =
                upload.getSuccessRecords() == null
                        ? 0
                        : upload.getSuccessRecords();

        upload.setSuccessRecords(
                currentSuccessRecords + chunk.size()
        );

        fileUploadRepository.save(upload);
    }
}