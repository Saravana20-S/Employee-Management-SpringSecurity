package com.bridgelabz.employeemanagement.repository;

import com.bridgelabz.employeemanagement.entity.Employee;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    List<Employee> findByUpload(FileUpload upload);

    boolean existsByEmployeeId(Long employeeId);
}