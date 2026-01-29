package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "proyectos")
@Data
public class Proyecto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    private String descripcion;
    private LocalDate fechaAsignacion = LocalDate.now();
    private LocalDate fechaLimite;
    
    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado = EstadoProyecto.PENDIENTE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Trabajador trabajador;
}