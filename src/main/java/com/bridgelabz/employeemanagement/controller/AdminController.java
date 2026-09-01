package com.bridgelabz.employeemanagement.controller;

import com.bridgelabz.employeemanagement.dto.response.*;
import com.bridgelabz.employeemanagement.service.AuditService;
import com.bridgelabz.employeemanagement.service.BatchService;
import com.bridgelabz.employeemanagement.service.FileUploadService;
import com.bridgelabz.employeemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final FileUploadService fileUploadService;

    private final AuditService auditService;
    private final BatchService batchService;


    @GetMapping("/users")
    public ResponseEntity<
            ApiResponse<List<UserResponseDTO>>>
    getAllUsers() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<UserResponseDTO>>builder()
                        .success(true)
                        .message("Users fetched successfully")
                        .data(userService.getAllUsers())
                        .build()
        );
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<
            ApiResponse<List<AuditLogResponseDTO>>>
    getAuditLogs() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<AuditLogResponseDTO>>builder()
                        .success(true)
                        .message("Audit logs fetched successfully")
                        .data(auditService.getAllAuditLogs())
                        .build()
        );
    }

    @GetMapping("/batches")
    public ResponseEntity<
            ApiResponse<List<FileUploadResponseDTO>>>
    getAllBatches() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<FileUploadResponseDTO>>builder()
                        .success(true)
                        .message("Batch information fetched successfully")
                        .data(fileUploadService.getAllUploads())
                        .build()
        );
    }


    @PutMapping("/users/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponseDTO>>
    activateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse
                        .<UserResponseDTO>builder()
                        .success(true)
                        .message("User activated successfully")
                        .data(userService.activateUser(id))
                        .build()
        );
    }


    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDTO>>
    deactivateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse
                        .<UserResponseDTO>builder()
                        .success(true)
                        .message("User deactivated successfully")
                        .data(userService.deactivateUser(id))
                        .build()
        );
    }


    @GetMapping("/uploads")
    public ResponseEntity<
            ApiResponse<List<FileUploadResponseDTO>>>
    getAllUploads() {

        return ResponseEntity.ok(
                ApiResponse
                        .<List<FileUploadResponseDTO>>builder()
                        .success(true)
                        .message("All uploads fetched successfully")
                        .data(fileUploadService.getAllUploads())
                        .build()
        );
    }
}