package com.project.autobackend1.service.impl;

import com.project.autobackend1.entity.dto.LoginRequest;
import com.project.autobackend1.entity.dto.LoginResponse;
import com.project.autobackend1.entity.usuario;
import com.project.autobackend1.repository.UsuarioRepository;
import com.project.autobackend1.service.AuthService;
import com.project.autobackend1.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(usuario);
        return new LoginResponse(token);
    }
}
