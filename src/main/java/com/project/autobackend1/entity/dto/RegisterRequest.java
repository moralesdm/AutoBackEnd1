package com.project.autobackend1.entity.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
}
