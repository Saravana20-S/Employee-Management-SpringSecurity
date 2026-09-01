package com.bridgelabz.employeemanagement.repository;

import com.bridgelabz.employeemanagement.entity.FailedEmployeeRecord;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailedEmployeeRecordRepository
        extends JpaRepository<FailedEmployeeRecord, Long> {

    List<FailedEmployeeRecord> findByUpload(
            FileUpload upload
    );
}