package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "usuarios")
@Data @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String role = "ROLE_ADMIN";
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;
}