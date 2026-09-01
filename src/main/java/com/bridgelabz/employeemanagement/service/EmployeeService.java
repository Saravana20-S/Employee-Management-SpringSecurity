package com.bridgelabz.employeemanagement.service;

import com.bridgelabz.employeemanagement.dto.response.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDTO> getEmployeesByUpload(Long uploadId);

    List<EmployeeResponseDTO> getAllEmployees();
}