package com.bridgelabz.employeemanagement.service.impl;

import com.bridgelabz.employeemanagement.dto.response.EmployeeResponseDTO;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.exception.ResourceNotFoundException;
import com.bridgelabz.employeemanagement.mapper.EmployeeMapper;
import com.bridgelabz.employeemanagement.repository.EmployeeRepository;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import com.bridgelabz.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final FileUploadRepository fileUploadRepository;

    @Override
    public List<EmployeeResponseDTO> getEmployeesByUpload(Long uploadId) {

        FileUpload upload = fileUploadRepository.findById(uploadId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Upload not found with id: " + uploadId
                        )
                );

        return employeeRepository.findByUpload(upload)
                .stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }
}