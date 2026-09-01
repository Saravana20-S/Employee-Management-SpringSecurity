package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.entity.Employee;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.enums.Department;
import com.bridgelabz.employeemanagement.exception.FileProcessingException;
import com.bridgelabz.employeemanagement.model.EmployeeExcelRow;
import com.bridgelabz.employeemanagement.repository.EmployeeRepository;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.util.regex.Pattern;

public class EmployeeProcessor
        implements ItemProcessor<EmployeeExcelRow, Employee> {

    private final EmployeeRepository employeeRepository;

    private final FileUpload upload;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@(.+)$"
            );

    private static final Pattern NAME_PATTERN =
            Pattern.compile(
                    "^[A-Za-z ]+$"
            );

    public EmployeeProcessor(
            EmployeeRepository employeeRepository,
            FileUpload upload) {

        this.employeeRepository =
                employeeRepository;

        this.upload = upload;
    }

    @Override
    public Employee process(
            EmployeeExcelRow item) {

        validateEmployee(item);

        if (employeeRepository
                .existsByEmployeeId(
                        item.getEmployeeId())) {

            throw new FileProcessingException(
                    "Duplicate Employee ID: "
                            + item.getEmployeeId()
            );
        }

        Department department;

        try {

            department =
                    Department.valueOf(
                            item.getDepartment()
                                    .toUpperCase()
                    );

        } catch (Exception exception) {

            throw new FileProcessingException(
                    "Invalid Department: "
                            + item.getDepartment()
            );
        }

        return Employee.builder()
                .employeeId(
                        item.getEmployeeId()
                )
                .name(item.getName())
                .email(item.getEmail())
                .department(department)
                .salary(item.getSalary())
                .upload(upload)
                .build();
    }

    private void validateEmployee(
            EmployeeExcelRow employee) {

        if (employee.getEmployeeId() == null) {

            throw new FileProcessingException(
                    "Employee ID is required"
            );
        }

        if (employee.getName() == null ||
                employee.getName().isBlank()) {

            throw new FileProcessingException(
                    "Employee name is required"
            );
        }

        if (!NAME_PATTERN
                .matcher(employee.getName())
                .matches()) {

            throw new FileProcessingException(
                    "Invalid employee name"
            );
        }

        if (employee.getEmail() == null ||
                !EMAIL_PATTERN
                        .matcher(employee.getEmail())
                        .matches()) {

            throw new FileProcessingException(
                    "Invalid email"
            );
        }

        if (employee.getDepartment() == null ||
                employee.getDepartment().isBlank()) {

            throw new FileProcessingException(
                    "Department is required"
            );
        }

        if (employee.getSalary() == null ||
                employee.getSalary()
                        .signum() <= 0) {

            throw new FileProcessingException(
                    "Salary must be greater than zero"
            );
        }
    }
}