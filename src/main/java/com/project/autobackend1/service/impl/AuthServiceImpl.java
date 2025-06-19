package com.project.autobackend1.service.impl;

import com.project.autobackend1.entity.dto.LoginRequest;
import com.project.autobackend1.entity.dto.LoginResponse;
import com.project.autobackend1.entity.dto.RefreshTokenRequest;
import com.project.autobackend1.entity.dto.RegisterRequest;
import com.project.autobackend1.entity.usuario;
import com.project.autobackend1.repository.UsuarioRepository;
import com.project.autobackend1.service.AuthService;
import com.project.autobackend1.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public LoginResponse register(RegisterRequest request) {
        Optional<usuario> existente = usuarioRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        usuario nuevo = usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .pais(request.getPais())
                .rol("CLIENTE")  // o "USER", según tu lógica
                .estado(true)
                .build();

        usuarioRepository.save(nuevo);
        String token = jwtUtil.generateToken(nuevo);
        return new LoginResponse(token);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String email = jwtUtil.extractUsername(request.getToken());

        usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el token actual aún sea válido (opcional según política)
        if (!jwtUtil.isTokenValid(request.getToken(), usuario)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        // Generar un nuevo token JWT
        String newToken = jwtUtil.generateToken(usuario);
        return new LoginResponse(newToken);
    }
}
