package com.project.autobackend1.service.impl;

import com.project.autobackend1.entity.PasswordResetToken;
import com.project.autobackend1.entity.RevokedToken;
import com.project.autobackend1.entity.dto.*;
import com.project.autobackend1.entity.usuario;
import com.project.autobackend1.repository.PasswordResetTokenRepository;
import com.project.autobackend1.repository.RevokedTokenRepository;
import com.project.autobackend1.repository.UsuarioRepository;
import com.project.autobackend1.service.AuthService;
import com.project.autobackend1.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Override
    public void logout(LogoutRequest request) {
        RevokedToken revoked = RevokedToken.builder()
                .token(request.getToken())
                .build();
        revokedTokenRepository.save(revoked);
    }

    @Override
    public UserResponse getProfile(String token) {
        String email = jwtUtil.extractUsername(token);

        usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UserResponse.builder()
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .ciudad(usuario.getCiudad())
                .pais(usuario.getPais())
                .rol(usuario.getRol())
                .build();
    }

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Override
    public void ForgotPassword(ForgotPasswordRequest request) {
        usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .usuario(usuario)
                .build();

        resetTokenRepository.save(resetToken);

        // Simula el envío por consola (puedes reemplazar con un envío real)
        System.out.println("🔐 Token de recuperación para " + usuario.getEmail() + ": " + token);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);

        // Opcional: eliminar el token después de usarlo
        resetTokenRepository.delete(resetToken);
    }

    @Override
    public List<UserAResponse> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserAResponse getUsuarioById(int id) {
        usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToResponse(usuario);
    }

        @Override
    public void deleteUsuario(int id) {
        usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    private UserAResponse mapToResponse(usuario u) {
        return UserAResponse.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .email(u.getEmail())
                .telefono(u.getTelefono())
                .direccion(u.getDireccion())
                .ciudad(u.getCiudad())
                .pais(u.getPais())
                .rol(u.getRol())
                .estado(u.getEstado())
                .build();
    }

    @Override
    public UserAResponse updateUsuario(int id, UsuarioUpdateRequest request) {
        usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getDireccion() != null) {
            usuario.setDireccion(request.getDireccion());
        }
        if (request.getCiudad() != null) {
            usuario.setCiudad(request.getCiudad());
        }
        if (request.getPais() != null) {
            usuario.setPais(request.getPais());
        }

        return mapToResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void activarUsuario(int id) {
        usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getEstado()==true) {
            throw new RuntimeException("El usuario ya está activo");
        }

        usuario.setEstado(true);
        usuarioRepository.save(usuario);
    }



}
