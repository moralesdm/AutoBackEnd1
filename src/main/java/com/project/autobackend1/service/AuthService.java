package com.project.autobackend1.service;

import com.project.autobackend1.entity.dto.LoginRequest;
import com.project.autobackend1.entity.dto.LoginResponse;
import com.project.autobackend1.entity.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
}