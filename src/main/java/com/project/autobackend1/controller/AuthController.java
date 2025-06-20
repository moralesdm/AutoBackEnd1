package com.project.autobackend1.controller;

import com.project.autobackend1.entity.dto.*;
import com.project.autobackend1.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/refresh-token")
    public LoginResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
    authService.logout(request);
    return ResponseEntity.ok().body("Logout Sucesfull");
    }

    @GetMapping("/me")
    public UserResponse me(@RequestHeader("Authorization") String authHeader) {
        // Extraer el token (eliminar "Bearer " si viene incluido)
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return authService.getProfile(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.ForgotPassword(request);
        return ResponseEntity.ok("Recovery token sent (check console or email)");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Contraseña restablecida exitosamente");
    }
}