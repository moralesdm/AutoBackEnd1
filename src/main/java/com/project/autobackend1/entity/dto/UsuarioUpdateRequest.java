package com.project.autobackend1.entity.dto;

import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
}
