package com.Project.Ems.DTO;

import com.Project.Ems.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class
EmployeeRequest {

    //@NotBlank(message = "Name is required")
    private String name;

    //@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    private String address;

    @Column(unique = true)
    //@NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    //@NotBlank(message = "Password is required")
    private String password;

    //@NotBlank(message = "Department is required")
    private String department;

    //@NotBlank(message = "Designation is required")
    private String designation;

    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    private Role role;


    private double salary;

}
