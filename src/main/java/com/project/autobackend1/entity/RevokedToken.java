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
@Table(name="revokeToken")
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500,unique = true)
    private String token;
    private LocalDateTime revocadoEn;
    @PrePersist
    public void prePersist(){
        if(this.revocadoEn == null){
            this.revocadoEn = LocalDateTime.now();
        }
    }
}
