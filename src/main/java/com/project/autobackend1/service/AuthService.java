package com.project.autobackend1.service;

import com.project.autobackend1.entity.dto.*;

import java.util.List;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(LogoutRequest request);
    UserResponse getProfile(String token);
    void ForgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    List<UserAResponse> getAllUsuarios();
    UserAResponse getUsuarioById(int id);

    UserAResponse updateUsuario(int id, UsuarioUpdateRequest request);

    void deleteUsuario(int id);
}