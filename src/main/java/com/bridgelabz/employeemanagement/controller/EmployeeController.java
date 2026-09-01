package com.bridgelabz.employeemanagement.controller;

import com.bridgelabz.employeemanagement.dto.response.ApiResponse;
import com.bridgelabz.employeemanagement.dto.response.FileUploadResponseDTO;
import com.bridgelabz.employeemanagement.service.FileUploadService;
import com.bridgelabz.employeemanagement.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponseDTO>>
    uploadEmployeeFile(
            @RequestParam("file") MultipartFile file) {

        String email =
                SecurityUtil.getCurrentUserEmail();

        FileUploadResponseDTO response =
                fileUploadService.uploadEmployeeFile(
                        file,
                        email
                );

        return ResponseEntity.ok(
                ApiResponse
                        .<FileUploadResponseDTO>builder()
                        .success(true)
                        .message(
                                "Employee file uploaded successfully"
                        )
                        .data(response)
                        .build()
        );
    }
}