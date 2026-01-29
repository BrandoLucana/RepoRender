package com.example.demo.config;

import com.example.demo.entity.EstadoRegistro;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear usuario admin por defecto si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEstadoRegistro(EstadoRegistro.ACTIVO);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado exitosamente");
        } else {
            log.info("Usuario admin ya existe");
        }
    }
}
