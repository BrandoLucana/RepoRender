package com.example.demo.controller;

import com.example.demo.dto.TrabajadorDTO;
import com.example.demo.entity.EstadoRegistro;
import com.example.demo.service.TrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trabajadores")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class TrabajadorController {
    private final TrabajadorService service;

    // ========== CRUD BÁSICO ==========
    @GetMapping
    public List<TrabajadorDTO> listarTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TrabajadorDTO obtenerPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TrabajadorDTO crear(@RequestBody TrabajadorDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TrabajadorDTO actualizar(@PathVariable Long id, @RequestBody TrabajadorDTO dto) {
        return service.update(id, dto);
    }

    // ========== GESTIÓN DE ESTADOS ==========
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.ok(Map.of(
            "message", "Trabajador desactivado exitosamente",
            "id", id.toString(),
            "estado", "INACTIVO"
        ));
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Map<String, Object>> reactivar(@PathVariable Long id) {
        TrabajadorDTO reactivado = service.reactivar(id);
        return ResponseEntity.ok(Map.of(
            "message", "Trabajador reactivado exitosamente",
            "id", id.toString(),
            "estado", "ACTIVO",
            "trabajador", reactivado
        ));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoRegistro estado) {
        TrabajadorDTO actualizado = service.cambiarEstado(id, estado);
        return ResponseEntity.ok(Map.of(
            "message", String.format("Estado cambiado a %s exitosamente", estado),
            "id", id.toString(),
            "estado", estado.toString(),
            "trabajador", actualizado
        ));
    }

    // ========== ELIMINACIONES ==========
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarLogicamente(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(Map.of(
            "message", "Trabajador desactivado (eliminación lógica) exitosamente",
            "id", id.toString(),
            "nota", "El trabajador puede ser reactivado posteriormente"
        ));
    }

    @DeleteMapping("/{id}/permanente")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarPermanentemente(@PathVariable Long id) {
        service.eliminarPermanente(id);
        return ResponseEntity.ok(Map.of(
            "message", "Trabajador eliminado permanentemente del sistema",
            "id", id.toString(),
            "advertencia", "Esta acción NO se puede deshacer"
        ));
    }
}