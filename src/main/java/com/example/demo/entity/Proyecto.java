package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    
    @ManyToMany
    @JoinTable(
        name = "proyectos_trabajadores",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "trabajador_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Trabajador> trabajadores = new ArrayList<>();
}