package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Endpoint público - Sin autenticación requerida");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> authenticatedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint autenticado - Token válido");
        response.put("username", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("authenticated", auth.isAuthenticated());
        
        log.info("User authenticated: {}", auth.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Endpoint admin - Acceso permitido");
        response.put("username", auth.getName());
        response.put("role", auth.getAuthorities().toString());
        
        log.info("Admin access granted for: {}", auth.getName());
        return ResponseEntity.ok(response);
    }
}
