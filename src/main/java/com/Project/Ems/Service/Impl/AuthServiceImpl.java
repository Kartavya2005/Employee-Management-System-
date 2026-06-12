package com.Project.Ems.Service.Impl;

import com.Project.Ems.DTO.AuthResponse;
import com.Project.Ems.DTO.LoginRequest;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.Repository.EmployeeRepository;
import com.Project.Ems.Security.JwtService;
import com.Project.Ems.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


//    @Override
//    public AuthResponse register(RegisterRequest request) {
//        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already in use: " + request.getEmail());
//        }
//        Employee employee = Employee.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole())
//                .build();
//
//        employeeRepository.save(employee);
//
//        String token = jwtService.generateToken(
//                new User(
//                        employee.getEmail(),
//                        employee.getPassword(),
//                        Collections.singletonList(
//                                new SimpleGrantedAuthority("ROLE_" + employee.getRole().name())
//                        )
//                )
//        );
//
//        return new AuthResponse(token);
//
//    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        String token = jwtService.generateToken(
                employee.getEmail(),
                String.valueOf(employee.getRole())
        );

        return new AuthResponse(token);
    }
}
