package com.Project.Ems.Service.Impl;

import com.Project.Ems.DTO.AuthResponse;
import com.Project.Ems.DTO.LoginRequest;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.Repository.EmployeeRepository;
import com.Project.Ems.Security.JwtService;
import com.Project.Ems.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Employee employee =
                employeeRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() -> {
                            log.error(
                                    "User not found with email: {}",
                                    request.getEmail()
                            );
                            return new RuntimeException(
                                    "User not found"
                            );
                        });
        log.info(
                "User authenticated successfully: {}",
                employee.getEmail()
        );
        String token = jwtService.generateToken(
                employee.getEmail(),
                String.valueOf(employee.getRole())
        );
        log.info(
                "JWT token generated for user: {}",
                employee.getEmail()
        );
        return new AuthResponse(token);
    }
}
