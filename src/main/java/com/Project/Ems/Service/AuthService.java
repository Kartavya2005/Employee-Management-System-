package com.Project.Ems.Service;

import com.Project.Ems.DTO.AuthResponse;
import com.Project.Ems.DTO.LoginRequest;


public interface AuthService {

    AuthResponse login(LoginRequest request);
}
