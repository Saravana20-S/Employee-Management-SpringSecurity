package com.bridgelabz.employeemanagement.service;

import com.bridgelabz.employeemanagement.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO getCurrentUser(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO activateUser(Long userId);

    UserResponseDTO deactivateUser(Long userId);
}