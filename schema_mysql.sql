-- Script para crear la base de datos y tablas en MySQL (LOCAL)
-- SIN datos de ejemplo

-- ============================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ============================================
DROP DATABASE IF EXISTS Developers;
CREATE DATABASE Developers;
USE Developers;

-- ============================================
-- 2. CREACIÓN DE TABLAS
-- ============================================

-- Tabla: trabajadores
CREATE TABLE IF NOT EXISTS trabajadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    telefono VARCHAR(20),
    fecha_ingreso DATE DEFAULT (CURRENT_DATE),
    tipo_documento ENUM('DNI', 'CARNET_EXTRANJERIA', 'RUC_RIF') NOT NULL,
    numero_documento VARCHAR(15) NOT NULL,
    cargo ENUM(
        'PROGRAMADOR',
        'INGENIERO_SISTEMAS',
        'ANALISTA',
        'DISENADOR_UX_UI',
        'QA_TESTER',
        'DEVOPS',
        'JEFE_DE_PROYECTO'
    ),
    estado_registro ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: proyectos (SIN trabajador_id; relación via tabla puente)
CREATE TABLE IF NOT EXISTS proyectos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_asignacion DATE DEFAULT (CURRENT_DATE),
    fecha_limite DATE,
    estado ENUM('PENDIENTE', 'EN_PROGRESO', 'COMPLETADO', 'CANCELADO') DEFAULT 'PENDIENTE',
    estado_registro ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla puente: proyectos_trabajadores (máx 3 trabajadores por proyecto)
CREATE TABLE IF NOT EXISTS proyectos_trabajadores (
    proyecto_id BIGINT NOT NULL,
    trabajador_id BIGINT NOT NULL,
    PRIMARY KEY (proyecto_id, trabajador_id),
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE,
    FOREIGN KEY (trabajador_id) REFERENCES trabajadores(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: usuarios (para autenticación)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ROLE_ADMIN',
    estado_registro ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. ÍNDICES PARA MEJORAR RENDIMIENTO
-- ============================================

CREATE INDEX idx_trabajadores_email ON trabajadores(email);
CREATE INDEX idx_trabajadores_numero_documento ON trabajadores(numero_documento);
CREATE INDEX idx_trabajadores_cargo ON trabajadores(cargo);
CREATE INDEX idx_proyectos_trabajador_id ON proyectos_trabajadores(trabajador_id);
CREATE INDEX idx_proyectos_estado ON proyectos(estado);
CREATE INDEX idx_usuarios_username ON usuarios(username);

-- ============================================
-- 4. INSERTAR DATOS DE EJEMPLO (DESACTIVADO)
-- ============================================

-- Insertar un usuario admin por defecto
-- La contraseña 'admin123' está encriptada con BCrypt
-- INSERT INTO usuarios (username, password, role) VALUES 
-- ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKaWHNXOWe5MeDFu/pLGRZA1v7i', 'ROLE_ADMIN');

-- Insertar trabajadores de ejemplo
-- INSERT INTO trabajadores (nombre, apellido, email, telefono, tipo_documento, numero_documento, cargo) VALUES 
-- ('Juan', 'Pérez', 'juan.perez@email.com', '999888777', 'DNI', '12345678', 'PROGRAMADOR'),
-- ('María', 'García', 'maria.garcia@email.com', '987654321', 'CARNET_EXTRANJERIA', 'AB1234567', 'INGENIERO_SISTEMAS'),
-- ('Carlos', 'López', 'carlos.lopez@email.com', '456789123', 'RUC_RIF', '20123456789', 'JEFE_DE_PROYECTO');

-- =====================================================
-- 4.1 TRIGGER PARA LIMITAR A 3 TRABAJADORES POR PROYECTO
-- =====================================================
DELIMITER $$
CREATE TRIGGER trg_proyecto_max_trabajadores_insert
BEFORE INSERT ON proyectos_trabajadores
FOR EACH ROW
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM proyectos_trabajadores
    WHERE proyecto_id = NEW.proyecto_id;
    IF total >= 3 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Máximo 3 trabajadores por proyecto';
    END IF;
END$$

CREATE TRIGGER trg_proyecto_max_trabajadores_update
BEFORE UPDATE ON proyectos_trabajadores
FOR EACH ROW
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM proyectos_trabajadores
    WHERE proyecto_id = NEW.proyecto_id;
    IF total >= 3 AND (NEW.trabajador_id <> OLD.trabajador_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Máximo 3 trabajadores por proyecto';
    END IF;
END$$
DELIMITER ;

-- ============================================
-- 5. COMANDOS ÚTILES PARA DBEAVER
-- ============================================

-- Ver todas las tablas
-- SHOW TABLES;

-- Ver estructura de una tabla
-- DESCRIBE trabajadores;

-- Ver relaciones de claves foráneas
-- SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME 
-- FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
-- WHERE TABLE_SCHEMA = 'Developers';

-- Eliminar todas las tablas (CUIDADO: borra datos)
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS proyectos, trabajadores, usuarios;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- NOTAS IMPORTANTES:
-- 1. Ejecuta este script en DBeaver en tu conexión de Railway
-- 2. Primero se crea la BD con CREATE DATABASE
-- 3. Luego se usa con USE backend_developers
-- 4. Los ENUM de MySQL aseguran integridad de datos
-- 5. Las contraseñas están encriptadas con BCrypt
-- ============================================
