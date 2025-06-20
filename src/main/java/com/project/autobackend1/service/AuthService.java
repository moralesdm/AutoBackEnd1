package com.project.autobackend1.service;

import com.project.autobackend1.entity.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(LogoutRequest request);
    UserResponse getProfile(String token);
    void ForgotPassword(ForgotPasswordRequest request);
}