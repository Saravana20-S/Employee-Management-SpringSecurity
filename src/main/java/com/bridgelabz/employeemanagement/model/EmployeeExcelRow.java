package com.bridgelabz.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExcelRow {

    private Integer rowNumber;

    private Long employeeId;

    private String name;

    private String email;

    private String department;

    private BigDecimal salary;
}