package com.example.demo.service.impl;

import com.example.demo.dto.TrabajadorDTO;
import com.example.demo.entity.Trabajador;
import com.example.demo.entity.Cargo;
import com.example.demo.entity.EstadoRegistro;
import com.example.demo.repository.TrabajadorRepository;
import com.example.demo.service.TrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrabajadorServiceImpl implements TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;

    // MANTIENE solo trabajadores ACTIVOS
    @Override
    public List<TrabajadorDTO> findAll() {
        return trabajadorRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // CAMBIA: Usa findById en lugar de findByIdAndEstadoRegistro
    // para poder acceder a trabajadores INACTIVOS cuando sea necesario
    @Override
    public TrabajadorDTO findById(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        return toDTO(trabajador);
    }

    @Override
    public TrabajadorDTO create(TrabajadorDTO dto) {
        Trabajador entity = new Trabajador();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setFechaIngreso(dto.getFechaIngreso());
        entity.setCargo(dto.getCargo());
        entity.setEstadoRegistro(EstadoRegistro.ACTIVO);

        Trabajador saved = trabajadorRepository.save(entity);
        return toDTO(saved);
    }

    // CAMBIA: Usa findById para poder actualizar cualquier trabajador
    @Override
    public TrabajadorDTO update(Long id, TrabajadorDTO dto) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        
        trabajador.setNombre(dto.getNombre());
        trabajador.setApellido(dto.getApellido());
        trabajador.setEmail(dto.getEmail());
        trabajador.setTelefono(dto.getTelefono());
        trabajador.setFechaIngreso(dto.getFechaIngreso());
        trabajador.setCargo(dto.getCargo());
        
        // Permite cambiar el estado si se envía en el DTO
        if (dto.getEstadoRegistro() != null) {
            trabajador.setEstadoRegistro(dto.getEstadoRegistro());
        }

        Trabajador updated = trabajadorRepository.save(trabajador);
        return toDTO(updated);
    }

    // MANTIENE: Usa findById para poder desactivar cualquier trabajador
    @Override
    @Transactional
    public void deleteById(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        
        trabajador.setEstadoRegistro(EstadoRegistro.INACTIVO);
        trabajadorRepository.save(trabajador);
    }

    // MANTIENE solo trabajadores ACTIVOS por cargo
    @Override
    public List<TrabajadorDTO> findByCargo(Cargo cargo) {
        return trabajadorRepository.findByCargoAndEstadoRegistro(cargo, EstadoRegistro.ACTIVO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ========== NUEVOS MÉTODOS ==========
    
    @Override
    @Transactional
    public void desactivar(Long id) {
        // Llama a deleteById (ambos hacen lo mismo)
        deleteById(id);
    }

    @Override
    @Transactional
    public TrabajadorDTO reactivar(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        
        // Verificar que esté INACTIVO
        if (trabajador.getEstadoRegistro() != EstadoRegistro.INACTIVO) {
            throw new RuntimeException("El trabajador no está INACTIVO. Estado actual: " + trabajador.getEstadoRegistro());
        }
        
        trabajador.setEstadoRegistro(EstadoRegistro.ACTIVO);
        Trabajador reactivado = trabajadorRepository.save(trabajador);
        return toDTO(reactivado);
    }

    @Override
    @Transactional
    public TrabajadorDTO cambiarEstado(Long id, EstadoRegistro nuevoEstado) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        
        // Verificar que no sea el mismo estado
        if (trabajador.getEstadoRegistro() == nuevoEstado) {
            throw new RuntimeException(
                String.format("El trabajador ya está en estado %s", nuevoEstado)
            );
        }
        
        trabajador.setEstadoRegistro(nuevoEstado);
        Trabajador actualizado = trabajadorRepository.save(trabajador);
        return toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarPermanente(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
        
        // Verificar que esté INACTIVO antes de eliminar físicamente
        if (trabajador.getEstadoRegistro() != EstadoRegistro.INACTIVO) {
            throw new RuntimeException(
                "No se puede eliminar permanentemente un trabajador ACTIVO. Desactívelo primero."
            );
        }
        
        trabajadorRepository.delete(trabajador);
    }

    private TrabajadorDTO toDTO(Trabajador t) {
        TrabajadorDTO dto = new TrabajadorDTO();
        dto.setId(t.getId());
        dto.setNombre(t.getNombre());
        dto.setApellido(t.getApellido());
        dto.setEmail(t.getEmail());
        dto.setTelefono(t.getTelefono());
        dto.setFechaIngreso(t.getFechaIngreso());
        dto.setCargo(t.getCargo());
        dto.setEstadoRegistro(t.getEstadoRegistro());
        return dto;
    }
}