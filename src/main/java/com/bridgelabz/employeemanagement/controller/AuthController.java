package com.bridgelabz.employeemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login")
    public ResponseEntity<String> login() {

        return ResponseEntity.ok(
                "Use /oauth2/authorization/google to login"
        );
    }

    @GetMapping("/logout-success")
    public ResponseEntity<String> logoutSuccess() {

        return ResponseEntity.ok(
                "Logged out successfully"
        );
    }
}