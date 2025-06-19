package com.project.autobackend1.controller;

import com.project.autobackend1.entity.dto.LoginRequest;
import com.project.autobackend1.entity.dto.LoginResponse;
import com.project.autobackend1.entity.dto.RefreshTokenRequest;
import com.project.autobackend1.entity.dto.RegisterRequest;
import com.project.autobackend1.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
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

}