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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);

    }
}