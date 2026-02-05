package com.example.demo.service.impl;

import com.example.demo.dto.ProyectoDTO;
import com.example.demo.entity.Proyecto;
import com.example.demo.entity.EstadoProyecto;
import com.example.demo.entity.EstadoRegistro;
import com.example.demo.entity.Trabajador;
import com.example.demo.repository.ProyectoRepository;
import com.example.demo.repository.TrabajadorRepository;
import com.example.demo.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService {
    
    private final ProyectoRepository proyectoRepository;
    private final TrabajadorRepository trabajadorRepository;

    @Override
    public List<ProyectoDTO> findAll() {
        return proyectoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProyectoDTO findById(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        return toDTO(proyecto);
    }

    @Override
    public ProyectoDTO create(ProyectoDTO dto) {
        Proyecto entity = new Proyecto();
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaAsignacion(dto.getFechaAsignacion() != null ? dto.getFechaAsignacion() : java.time.LocalDate.now());
        entity.setFechaLimite(dto.getFechaLimite());
        entity.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoProyecto.PENDIENTE);
        entity.setEstadoRegistro(EstadoRegistro.ACTIVO);

        List<Long> ids = normalizarTrabajadorIds(dto);
        validarMaximoTrabajadores(ids);
        entity.setTrabajadores(obtenerTrabajadores(ids));

        Proyecto saved = proyectoRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public ProyectoDTO update(Long id, ProyectoDTO dto) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        
        proyecto.setTitulo(dto.getTitulo());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setFechaLimite(dto.getFechaLimite());
        proyecto.setEstado(dto.getEstado());

        if (dto.getTrabajadorId() != null || dto.getTrabajadorIds() != null) {
            List<Long> ids = normalizarTrabajadorIds(dto);
            validarMaximoTrabajadores(ids);
            proyecto.setTrabajadores(obtenerTrabajadores(ids));
        }
        
        // Permite cambiar el estado de registro si se envía en el DTO
        if (dto.getEstadoRegistro() != null) {
            proyecto.setEstadoRegistro(dto.getEstadoRegistro());
        }

        Proyecto updated = proyectoRepository.save(proyecto);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        
        proyecto.setEstadoRegistro(EstadoRegistro.INACTIVO);
        proyectoRepository.save(proyecto);
    }

    @Override
    public List<ProyectoDTO> findByTrabajadorId(Long trabajadorId) {
        return proyectoRepository.findByTrabajadoresIdAndEstadoRegistro(trabajadorId, EstadoRegistro.ACTIVO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoDTO> findByEstado(EstadoProyecto estado) {
        return proyectoRepository.findByEstadoAndEstadoRegistro(estado, EstadoRegistro.ACTIVO).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProyectoDTO updateEstado(Long id, EstadoProyecto estado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        proyecto.setEstado(estado);
        Proyecto updated = proyectoRepository.save(proyecto);
        return toDTO(updated);
    }

    // ========== NUEVOS MÉTODOS ==========
    
    @Override
    @Transactional
    public void desactivar(Long id) {
        deleteById(id); // Mismo comportamiento
    }

    @Override
    @Transactional
    public ProyectoDTO reactivar(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        
        // Verificar que esté INACTIVO
        if (proyecto.getEstadoRegistro() != EstadoRegistro.INACTIVO) {
            throw new RuntimeException("El proyecto no está INACTIVO. Estado actual: " + proyecto.getEstadoRegistro());
        }
        
        proyecto.setEstadoRegistro(EstadoRegistro.ACTIVO);
        Proyecto reactivado = proyectoRepository.save(proyecto);
        return toDTO(reactivado);
    }

    @Override
    @Transactional
    public ProyectoDTO cambiarEstadoRegistro(Long id, EstadoRegistro nuevoEstado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        
        // Verificar que no sea el mismo estado
        if (proyecto.getEstadoRegistro() == nuevoEstado) {
            throw new RuntimeException(
                String.format("El proyecto ya está en estado %s", nuevoEstado)
            );
        }
        
        proyecto.setEstadoRegistro(nuevoEstado);
        Proyecto actualizado = proyectoRepository.save(proyecto);
        return toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarPermanente(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
        
        // Verificar que esté INACTIVO antes de eliminar físicamente
        if (proyecto.getEstadoRegistro() != EstadoRegistro.INACTIVO) {
            throw new RuntimeException(
                "No se puede eliminar permanentemente un proyecto ACTIVO. Desactívelo primero."
            );
        }
        
        proyectoRepository.delete(proyecto);
    }

    private ProyectoDTO toDTO(Proyecto p) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setDescripcion(p.getDescripcion());
        dto.setFechaAsignacion(p.getFechaAsignacion());
        dto.setFechaLimite(p.getFechaLimite());
        dto.setEstado(p.getEstado());
        dto.setEstadoRegistro(p.getEstadoRegistro());
        if (p.getTrabajadores() != null && !p.getTrabajadores().isEmpty()) {
            List<Long> ids = p.getTrabajadores().stream()
                    .map(Trabajador::getId)
                    .collect(Collectors.toList());
            dto.setTrabajadorIds(ids);
            if (!ids.isEmpty()) {
                dto.setTrabajadorId(ids.get(0));
            }
        }
        return dto;
    }

    private List<Long> normalizarTrabajadorIds(ProyectoDTO dto) {
        Set<Long> ids = new LinkedHashSet<>();
        if (dto.getTrabajadorIds() != null) {
            ids.addAll(dto.getTrabajadorIds());
        }
        if (dto.getTrabajadorId() != null) {
            ids.add(dto.getTrabajadorId());
        }
        return ids.stream().collect(Collectors.toList());
    }

    private void validarMaximoTrabajadores(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("Debe asignar al menos un trabajador al proyecto");
        }
        if (ids.size() > 3) {
            throw new RuntimeException("Máximo 3 trabajadores por proyecto");
        }
    }

    private List<Trabajador> obtenerTrabajadores(List<Long> ids) {
        List<Trabajador> trabajadores = trabajadorRepository.findAllById(ids);
        if (trabajadores.size() != ids.size()) {
            throw new RuntimeException("Uno o más trabajadores no existen");
        }
        return trabajadores;
    }
}