package com.example.demo.repository;

import com.example.demo.entity.Usuario;
import com.example.demo.entity.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByUsernameAndEstadoRegistro(String username, EstadoRegistro estadoRegistro);
}