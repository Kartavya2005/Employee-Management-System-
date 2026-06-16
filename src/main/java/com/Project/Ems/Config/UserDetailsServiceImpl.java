package com.Project.Ems.Config;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.Exceptions.EmployeeNotFoundException;
import com.Project.Ems.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    public UserDetails loadUserByUsername(String email) {
        log.info("Loading user by email: {}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                {
                    log.warn("Authentication failed for email: {}", email);
                    return new EmployeeNotFoundException(
                            "Employee not found with email: " + email
                    );
                });

        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + employee.getRole().name())
                 )


        );
    }
}
