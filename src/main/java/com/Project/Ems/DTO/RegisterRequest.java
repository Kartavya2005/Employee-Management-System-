package com.Project.Ems.DTO;

import com.Project.Ems.Enum.Role;
import lombok.Data;


@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
