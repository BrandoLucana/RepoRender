-- Corrige el esquema de la tabla proyectos (elimina columna legacy trabajador_id)
-- Ejecuta en la BD que usa la app (Developers)

USE Developers;

-- Elimina la columna legacy que rompe el INSERT cuando se usa la tabla puente
ALTER TABLE proyectos
    DROP COLUMN trabajador_id;
