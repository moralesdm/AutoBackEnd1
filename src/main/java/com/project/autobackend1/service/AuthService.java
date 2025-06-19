package com.project.autobackend1.service;

import com.project.autobackend1.entity.dto.LoginRequest;
import com.project.autobackend1.entity.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}