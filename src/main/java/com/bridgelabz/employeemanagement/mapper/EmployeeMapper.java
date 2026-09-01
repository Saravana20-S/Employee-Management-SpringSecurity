package com.bridgelabz.employeemanagement.mapper;

import com.bridgelabz.employeemanagement.dto.response.EmployeeResponseDTO;
import com.bridgelabz.employeemanagement.entity.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static EmployeeResponseDTO toResponse(Employee employee) {

        if (employee == null) {
            return null;
        }

        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .employeeId(employee.getEmployeeId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .uploadId(
                        employee.getUpload() != null
                                ? employee.getUpload().getId()
                                : null
                )
                .build();
    }
}