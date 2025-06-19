package com.project.autobackend1.utils;

import com.project.autobackend1.entity.usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924"; // Usar variable de entorno en prod

    public String generateToken(usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}