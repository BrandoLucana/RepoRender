package com.example.demo.service;

import com.example.demo.dto.ProyectoDTO;
import com.example.demo.entity.EstadoProyecto;
import com.example.demo.entity.EstadoRegistro;
import java.util.List;

public interface ProyectoService {
    List<ProyectoDTO> findAll();
    ProyectoDTO findById(Long id);
    ProyectoDTO create(ProyectoDTO dto);
    ProyectoDTO update(Long id, ProyectoDTO dto);
    void deleteById(Long id);
    List<ProyectoDTO> findByTrabajadorId(Long trabajadorId);
    List<ProyectoDTO> findByEstado(EstadoProyecto estado);
    ProyectoDTO updateEstado(Long id, EstadoProyecto estado);
    
    // NUEVOS MÉTODOS PARA GESTIÓN DE ESTADOS
    void desactivar(Long id);
    ProyectoDTO reactivar(Long id);
    ProyectoDTO cambiarEstadoRegistro(Long id, EstadoRegistro nuevoEstado);
    
    // Eliminación física (solo para INACTIVOS)
    void eliminarPermanente(Long id);
}