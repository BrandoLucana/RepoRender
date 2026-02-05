package com.example.demo.repository;

import com.example.demo.entity.Proyecto;
import com.example.demo.entity.EstadoProyecto;
import com.example.demo.entity.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Proyecto> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
    List<Proyecto> findByTrabajadoresIdAndEstadoRegistro(Long trabajadorId, EstadoRegistro estadoRegistro);
    List<Proyecto> findByEstadoAndEstadoRegistro(EstadoProyecto estado, EstadoRegistro estadoRegistro);
}