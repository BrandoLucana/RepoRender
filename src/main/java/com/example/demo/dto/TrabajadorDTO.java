package com.example.demo.dto;
import com.example.demo.entity.Cargo;
import com.example.demo.entity.EstadoRegistro;
import com.example.demo.entity.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 8, max = 15, message = "El número de documento debe tener entre 8 y 15 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El número de documento solo puede contener letras y números")
    private String numeroDocumento;
    
    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate fechaIngreso;
    
    private EstadoRegistro estadoRegistro;
}