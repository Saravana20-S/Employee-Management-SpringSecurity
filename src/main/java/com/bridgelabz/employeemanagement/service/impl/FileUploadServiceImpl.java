package com.bridgelabz.employeemanagement.service.impl;

import com.bridgelabz.employeemanagement.dto.response.FileUploadResponseDTO;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.entity.User;
import com.bridgelabz.employeemanagement.enums.BatchStatus;
import com.bridgelabz.employeemanagement.exception.FileProcessingException;
import com.bridgelabz.employeemanagement.exception.ResourceNotFoundException;
import com.bridgelabz.employeemanagement.rabbitmq.EmployeeEventProducer;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import com.bridgelabz.employeemanagement.repository.UserRepository;
import com.bridgelabz.employeemanagement.service.FileUploadService;
import com.bridgelabz.employeemanagement.util.ExcelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadRepository fileUploadRepository;
    private final UserRepository userRepository;
    private final EmployeeEventProducer eventProducer;
    private final ExcelValidator excelValidator;

    @Value("${app.upload.directory}")
    private String uploadDirectory;

    @Override
    public FileUploadResponseDTO uploadEmployeeFile(
            MultipartFile file,
            String userEmail) {

        validateFile(file);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userEmail
                        )
                );

        try {

            Path directoryPath =
                    Paths.get(uploadDirectory);

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            String uniqueFileName =
                    UUID.randomUUID() + "_" +
                            file.getOriginalFilename();

            Path filePath =
                    directoryPath.resolve(uniqueFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            int totalRecords =
                    excelValidator.countRecords(
                            filePath.toString()
                    );

            FileUpload upload =
                    FileUpload.builder()
                            .fileName(file.getOriginalFilename())
                            .filePath(filePath.toString())
                            .uploadedBy(user)
                            .totalRecords(totalRecords)
                            .successRecords(0)
                            .failedRecords(0)
                            .status(BatchStatus.UPLOADED)
                            .build();

            FileUpload savedUpload =
                    fileUploadRepository.save(upload);

            eventProducer.publishFileUploadedEvent(
                    savedUpload
            );

            return mapToResponse(savedUpload);

        } catch (IOException exception) {

            throw new FileProcessingException(
                    "Failed to upload file",
                    exception
            );
        }
    }

    @Override
    public List<FileUploadResponseDTO> getUserUploads(
            String userEmail) {

        User user =
                userRepository.findByEmail(userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return fileUploadRepository
                .findByUploadedBy(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FileUploadResponseDTO getUploadById(
            Long uploadId) {

        FileUpload upload =
                fileUploadRepository.findById(uploadId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Upload not found"
                                )
                        );

        return mapToResponse(upload);
    }

    @Override
    public List<FileUploadResponseDTO> getAllUploads() {

        return fileUploadRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileProcessingException(
                    "File cannot be empty"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                !fileName.toLowerCase().endsWith(".xlsx")) {

            throw new FileProcessingException(
                    "Only .xlsx files are allowed"
            );
        }

        excelValidator.validateHeaders(file);
    }

    private FileUploadResponseDTO mapToResponse(
            FileUpload upload) {

        return FileUploadResponseDTO.builder()
                .id(upload.getId())
                .fileName(upload.getFileName())
                .totalRecords(upload.getTotalRecords())
                .successRecords(upload.getSuccessRecords())
                .failedRecords(upload.getFailedRecords())
                .status(upload.getStatus())
                .createdAt(upload.getCreatedAt())
                .completedAt(upload.getCompletedAt())
                .build();
    }
}