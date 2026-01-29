package com.example.demo.dto;
import com.example.demo.entity.Cargo;
import com.example.demo.entity.EstadoRegistro;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TrabajadorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Cargo cargo;
    
    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate fechaIngreso;
    
    private EstadoRegistro estadoRegistro;
}