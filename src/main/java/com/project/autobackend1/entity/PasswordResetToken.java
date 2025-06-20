package com.project.autobackend1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="passwordResetToken")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500,unique = true)
    private String token;
    @OneToOne
    private usuario usuario;
    private LocalDateTime expiracion;
    @PrePersist
    public void setExpiracion(){
        this.expiracion = LocalDateTime.now().plusMinutes(15);
    }

}
