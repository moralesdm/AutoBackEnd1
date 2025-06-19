package com.project.autobackend1.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String rol;
}