package com.bridgelabz.employeemanagement.repository;

import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository
        extends JpaRepository<FileUpload, Long> {

    List<FileUpload> findByUploadedBy(User user);

    List<FileUpload> findAllByOrderByCreatedAtDesc();
}