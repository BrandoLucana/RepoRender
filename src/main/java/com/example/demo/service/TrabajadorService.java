package com.example.demo.service;

import com.example.demo.dto.TrabajadorDTO;
import com.example.demo.entity.Cargo;
import com.example.demo.entity.EstadoRegistro;
import java.util.List;

public interface TrabajadorService {
    List<TrabajadorDTO> findAll();
    TrabajadorDTO findById(Long id);
    TrabajadorDTO create(TrabajadorDTO dto);
    TrabajadorDTO update(Long id, TrabajadorDTO dto);
    void deleteById(Long id);  // Eliminación lógica (desactiva)
    List<TrabajadorDTO> findByCargo(Cargo cargo);
    
    // NUEVOS MÉTODOS PARA GESTIÓN DE ESTADOS
    void desactivar(Long id);
    TrabajadorDTO reactivar(Long id);
    TrabajadorDTO cambiarEstado(Long id, EstadoRegistro nuevoEstado);
    
    // Eliminación física (solo para INACTIVOS)
    void eliminarPermanente(Long id);
}