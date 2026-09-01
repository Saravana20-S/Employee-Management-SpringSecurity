package com.bridgelabz.employeemanagement.entity;

import com.bridgelabz.employeemanagement.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_uploads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    private Integer totalRecords;

    private Integer successRecords;

    private Integer failedRecords;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = BatchStatus.UPLOADED;
        }

        if (totalRecords == null) {
            totalRecords = 0;
        }

        if (successRecords == null) {
            successRecords = 0;
        }

        if (failedRecords == null) {
            failedRecords = 0;
        }
    }
}