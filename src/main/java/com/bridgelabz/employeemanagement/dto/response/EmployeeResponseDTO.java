package com.bridgelabz.employeemanagement.dto.response;

import com.bridgelabz.employeemanagement.enums.Department;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;

    private Long employeeId;

    private String name;

    private String email;

    private Department department;

    private BigDecimal salary;

    private Long uploadId;
}