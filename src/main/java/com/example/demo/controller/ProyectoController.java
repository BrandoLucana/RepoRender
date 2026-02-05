package com.example.demo.controller;

import com.example.demo.dto.ProyectoDTO;
import com.example.demo.service.ProyectoService;
import com.example.demo.entity.EstadoProyecto;
import com.example.demo.entity.EstadoRegistro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {
    private final ProyectoService service;

    // ========== CRUD BÁSICO ==========
    @GetMapping
    public List<ProyectoDTO> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProyectoDTO obtenerPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ProyectoDTO crear(@RequestBody ProyectoDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProyectoDTO actualizar(@PathVariable Long id, @RequestBody ProyectoDTO dto) {
        return service.update(id, dto);
    }

    // ========== CONSULTAS ESPECÍFICAS ==========
    @GetMapping("/trabajador/{trabajadorId}")
    public List<ProyectoDTO> buscarPorTrabajador(@PathVariable Long trabajadorId) {
        return service.findByTrabajadorId(trabajadorId);
    }

    @GetMapping("/estado-proyecto/{estado}")
    public List<ProyectoDTO> buscarPorEstadoProyecto(@PathVariable EstadoProyecto estado) {
        return service.findByEstado(estado);
    }

    // ========== GESTIÓN DE ESTADO PROYECTO ==========
    @PatchMapping("/{id}/estado-proyecto")
    public ProyectoDTO actualizarEstadoProyecto(
            @PathVariable Long id, 
            @RequestBody Map<String, EstadoProyecto> body) {
        return service.updateEstado(id, body.get("estado"));
    }

    // ========== GESTIÓN DE ESTADO REGISTRO ==========
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.ok(Map.of(
            "message", "Proyecto desactivado exitosamente",
            "id", id.toString(),
            "estadoRegistro", "INACTIVO"
        ));
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Map<String, Object>> reactivar(@PathVariable Long id) {
        ProyectoDTO reactivado = service.reactivar(id);
        return ResponseEntity.ok(Map.of(
            "message", "Proyecto reactivado exitosamente",
            "id", id.toString(),
            "estadoRegistro", "ACTIVO",
            "proyecto", reactivado
        ));
    }

    @PatchMapping("/{id}/estado-registro")
    public ResponseEntity<Map<String, Object>> cambiarEstadoRegistro(
            @PathVariable Long id,
            @RequestParam EstadoRegistro estado) {
        ProyectoDTO actualizado = service.cambiarEstadoRegistro(id, estado);
        return ResponseEntity.ok(Map.of(
            "message", String.format("Estado de registro cambiado a %s exitosamente", estado),
            "id", id.toString(),
            "estadoRegistro", estado.toString(),
            "proyecto", actualizado
        ));
    }

    // ========== ELIMINACIONES ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarLogicamente(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(Map.of(
            "message", "Proyecto desactivado (eliminación lógica) exitosamente",
            "id", id.toString(),
            "nota", "El proyecto puede ser reactivado posteriormente"
        ));
    }

    @DeleteMapping("/{id}/permanente")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarPermanentemente(@PathVariable Long id) {
        service.eliminarPermanente(id);
        return ResponseEntity.ok(Map.of(
            "message", "Proyecto eliminado permanentemente del sistema",
            "id", id.toString(),
            "advertencia", "Esta acción NO se puede deshacer"
        ));
    }
}