package com.bridgelabz.employeemanagement.service;

import com.bridgelabz.employeemanagement.dto.response.FileUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    FileUploadResponseDTO uploadEmployeeFile(
            MultipartFile file,
            String userEmail
    );

    List<FileUploadResponseDTO> getUserUploads(String userEmail);

    FileUploadResponseDTO getUploadById(Long uploadId);

    List<FileUploadResponseDTO> getAllUploads();
}