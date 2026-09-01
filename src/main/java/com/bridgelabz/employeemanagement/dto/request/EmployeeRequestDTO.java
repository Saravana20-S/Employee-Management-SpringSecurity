package com.bridgelabz.employeemanagement.dto.request;

import com.bridgelabz.employeemanagement.enums.Department;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Employee name is required")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Name must contain only valid characters"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "Salary is required")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Salary must be greater than 0"
    )
    private BigDecimal salary;
}