package com.example.demo.repository;

import com.example.demo.entity.Trabajador;
import com.example.demo.entity.Cargo;
import com.example.demo.entity.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
    List<Trabajador> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Trabajador> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
    List<Trabajador> findByCargoAndEstadoRegistro(Cargo cargo, EstadoRegistro estadoRegistro);
    List<Trabajador> findByNombreContainingIgnoreCaseAndEstadoRegistro(String nombre, EstadoRegistro estadoRegistro);
}