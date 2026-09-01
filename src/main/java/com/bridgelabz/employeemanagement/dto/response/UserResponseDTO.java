package com.bridgelabz.employeemanagement.dto.response;

import com.bridgelabz.employeemanagement.enums.Role;
import com.bridgelabz.employeemanagement.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String provider;

    private Role role;

    private UserStatus status;

    private LocalDateTime createdAt;
}