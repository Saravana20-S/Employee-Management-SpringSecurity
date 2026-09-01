package com.bridgelabz.employeemanagement.service.impl;

import com.bridgelabz.employeemanagement.dto.response.UserResponseDTO;
import com.bridgelabz.employeemanagement.entity.User;
import com.bridgelabz.employeemanagement.enums.UserStatus;
import com.bridgelabz.employeemanagement.exception.ResourceNotFoundException;
import com.bridgelabz.employeemanagement.repository.UserRepository;
import com.bridgelabz.employeemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponseDTO activateUser(Long userId) {

        User user = getUser(userId);

        user.setStatus(UserStatus.ACTIVE);

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDTO deactivateUser(Long userId) {

        User user = getUser(userId);

        user.setStatus(UserStatus.INACTIVE);

        return mapToResponse(userRepository.save(user));
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }

    private UserResponseDTO mapToResponse(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .provider(user.getProvider())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}