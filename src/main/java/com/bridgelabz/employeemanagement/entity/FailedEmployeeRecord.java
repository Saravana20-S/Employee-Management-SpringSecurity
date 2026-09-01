package com.bridgelabz.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_employee_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedEmployeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id")
    private FileUpload upload;

    private Integer rowNumber;

    @Column(columnDefinition = "TEXT")
    private String employeeData;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}