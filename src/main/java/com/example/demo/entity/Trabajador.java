package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
 

@Entity @Table(name = "trabajadores")
@Data

public class Trabajador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaIngreso = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, length = 15)
    private String numeroDocumento;
    
    @Enumerated(EnumType.STRING)
    private Cargo cargo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;
    
    @ManyToMany(mappedBy = "trabajadores")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Proyecto> proyectos = new ArrayList<>();
}