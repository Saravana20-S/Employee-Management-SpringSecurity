package com.bridgelabz.employeemanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedRecordResponseDTO {

    private Long id;

    private Integer rowNumber;

    private String employeeData;

    private String errorMessage;
}