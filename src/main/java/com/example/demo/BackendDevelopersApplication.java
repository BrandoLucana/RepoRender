package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@SpringBootApplication
public class BackendDevelopersApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDevelopersApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                repo.save(admin);
                System.out.println("Usuario admin creado → usuario: admin / contraseña: admin123");
            }
        };
    }
}