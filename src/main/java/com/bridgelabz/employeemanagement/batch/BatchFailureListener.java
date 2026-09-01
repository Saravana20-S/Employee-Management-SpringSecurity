package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.entity.FailedEmployeeRecord;
import com.bridgelabz.employeemanagement.entity.FileUpload;
import com.bridgelabz.employeemanagement.repository.FailedEmployeeRecordRepository;
import com.bridgelabz.employeemanagement.repository.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.listener.SkipListener;


@RequiredArgsConstructor
public class BatchFailureListener
        implements SkipListener<Object, Object> {

    private final FailedEmployeeRecordRepository
            failedRecordRepository;

    private final FileUploadRepository
            fileUploadRepository;

    private final FileUpload upload;

    @Override
    public void onSkipInRead(
            Throwable throwable) {
    }

    @Override
    public void onSkipInWrite(
            Object item,
            Throwable throwable) {

        saveFailedRecord(
                item,
                throwable
        );
    }

    @Override
    public void onSkipInProcess(
            Object item,
            Throwable throwable) {

        saveFailedRecord(
                item,
                throwable
        );
    }

    private void saveFailedRecord(
            Object item,
            Throwable throwable) {

        FailedEmployeeRecord failedRecord =
                FailedEmployeeRecord.builder()
                        .upload(upload)
                        .rowNumber(null)
                        .employeeData(
                                String.valueOf(item)
                        )
                        .errorMessage(
                                throwable.getMessage()
                        )
                        .build();

        failedRecordRepository.save(
                failedRecord
        );

        upload.setFailedRecords(
                upload.getFailedRecords()
                        + 1
        );

        fileUploadRepository.save(upload);
    }
}