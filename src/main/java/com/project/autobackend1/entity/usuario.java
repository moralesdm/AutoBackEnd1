
package com.project.autobackend1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String apellido;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String rol;
    private Boolean estado;
    private LocalDateTime fecha_registro;

    @PrePersist
    public void prePersist(){
        this.fecha_registro = LocalDateTime.now();
        this.estado = true;
    }
}
