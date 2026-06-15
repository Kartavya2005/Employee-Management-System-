package com.Project.Ems.controller;

import com.Project.Ems.DTO.AuthResponse;
import com.Project.Ems.DTO.LoginRequest;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.Exceptions.InvalidCredentialsException;
import com.Project.Ems.Repository.EmployeeRepository;
import com.Project.Ems.Security.JwtService;
import com.Project.Ems.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Validate request
        log.info("Login request received for email: {}", request.getEmail());
        if (request.getEmail() == null || request.getPassword() == null) {
            log.warn("Login request failed for email: {}. Email and password are required.", request.getEmail());
            throw new InvalidCredentialsException("Email and password are required");
        }
            AuthResponse response = authService.login(request);
        log.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);

    }
}