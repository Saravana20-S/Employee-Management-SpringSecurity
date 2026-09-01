package com.bridgelabz.employeemanagement.controller;

import com.bridgelabz.employeemanagement.dto.response.*;
import com.bridgelabz.employeemanagement.service.BatchService;
import com.bridgelabz.employeemanagement.service.FileUploadService;
import com.bridgelabz.employeemanagement.service.UserService;
import com.bridgelabz.employeemanagement.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final BatchService batchService;


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO>>
    getCurrentUser() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        UserResponseDTO user =
                userService.getCurrentUser(email);

        return ResponseEntity.ok(
                ApiResponse
                        .<UserResponseDTO>builder()
                        .success(true)
                        .message("Current user fetched")
                        .data(user)
                        .build()
        );
    }


    @GetMapping("/uploads")
    public ResponseEntity<
            ApiResponse<List<FileUploadResponseDTO>>>
    getUserUploads() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        List<FileUploadResponseDTO> uploads =
                fileUploadService.getUserUploads(email);

        return ResponseEntity.ok(
                ApiResponse
                        .<List<FileUploadResponseDTO>>builder()
                        .success(true)
                        .message("Uploads fetched successfully")
                        .data(uploads)
                        .build()
        );
    }


    @GetMapping("/uploads/{id}/status")
    public ResponseEntity<
            ApiResponse<BatchStatusResponseDTO>>
    getUploadStatus(
            @PathVariable Long id) {

        BatchStatusResponseDTO status =
                batchService.getBatchStatus(id);

        return ResponseEntity.ok(
                ApiResponse
                        .<BatchStatusResponseDTO>builder()
                        .success(true)
                        .message("Batch status fetched")
                        .data(status)
                        .build()
        );
    }


    @GetMapping("/uploads/{id}/errors")
    public ResponseEntity<
            ApiResponse<List<FailedRecordResponseDTO>>>
    getFailedRecords(
            @PathVariable Long id) {

        List<FailedRecordResponseDTO> records =
                batchService.getFailedRecords(id);

        return ResponseEntity.ok(
                ApiResponse
                        .<List<FailedRecordResponseDTO>>builder()
                        .success(true)
                        .message("Failed records fetched")
                        .data(records)
                        .build()
        );
    }
}