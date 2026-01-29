package com.example.demo.dto;
import com.example.demo.entity.EstadoProyecto;
import com.example.demo.entity.EstadoRegistro;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    
    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate fechaAsignacion;
    
    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate fechaLimite;
    
    private EstadoProyecto estado;
    private Long trabajadorId;
    private EstadoRegistro estadoRegistro;
}