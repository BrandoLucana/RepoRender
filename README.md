# Backend Developers - API REST con Spring Boot

Sistema de gestión de trabajadores y proyectos con autenticación JWT.

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Security** con JWT
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Maven**

## 📋 Características

- ✅ Autenticación con JWT
- ✅ CRUD completo de Trabajadores
- ✅ CRUD completo de Proyectos
- ✅ **Eliminado lógico (Soft Delete)** - Los registros no se eliminan permanentemente
- ✅ Gestión de estados de proyectos
- ✅ Asignación de proyectos a trabajadores
- ✅ Seguridad basada en roles (ROLE_ADMIN)

## 🔧 Configuración

### 1. Base de datos

Crear la base de datos MySQL:

```sql
CREATE DATABASE Developers;
```

### 2. Configuración

Edita `src/main/resources/application.properties` si es necesario:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Developers
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

## 🔐 Autenticación

### Usuario por defecto

Al iniciar la aplicación, se crea automáticamente:
- **Usuario:** admin
- **Contraseña:** admin123
- **Role:** ROLE_ADMIN

### Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### Usar el token

Incluir en todas las peticiones protegidas:
```
Authorization: Bearer {token}
```

## 📚 Endpoints

### Trabajadores (`/api/trabajadores`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/trabajadores` | Listar todos |
| GET | `/api/trabajadores/{id}` | Obtener por ID |
| POST | `/api/trabajadores` | Crear nuevo |
| PUT | `/api/trabajadores/{id}` | Actualizar |
| DELETE | `/api/trabajadores/{id}` | Eliminar |

**Ejemplo crear trabajador:**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "telefono": "123456789",
  "cargo": "PROGRAMADOR",
  "fechaIngreso": "2024-01-15"
}
```

**Cargos disponibles:**
- PROGRAMADOR
- INGENIERO_SISTEMAS
- ANALISTA
- DISENADOR_UX_UI
- QA_TESTER
- DEVOPS
- JEFE_DE_PROYECTO

### Proyectos (`/api/proyectos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/proyectos` | Listar todos |
| GET | `/api/proyectos/{id}` | Obtener por ID |
| POST | `/api/proyectos` | Crear nuevo |
| PUT | `/api/proyectos/{id}` | Actualizar |
| DELETE | `/api/proyectos/{id}` | Eliminar |
| GET | `/api/proyectos/trabajador/{id}` | Por trabajador |
| GET | `/api/proyectos/estado/{estado}` | Por estado |
| PATCH | `/api/proyectos/{id}/estado` | Actualizar estado |

**Ejemplo crear proyecto:**
```json
{
  "titulo": "Sistema de Inventario",
  "descripcion": "Desarrollo de sistema web para gestión de inventario",
  "fechaAsignacion": "2024-01-20",
  "fechaLimite": "2024-06-30",
  "estado": "PENDIENTE",
  "trabajadorId": 1
}
```

**Estados disponibles:**
- PENDIENTE
- EN_PROGRESO
- COMPLETADO
- CANCELADO

**Actualizar estado:**
```json
PATCH /api/proyectos/1/estado
{
  "estado": "EN_PROGRESO"
}
```

## 🏗️ Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── ProyectoController.java
│   └── TrabajadorController.java
├── dto/
│   ├── JwtResponse.java
│   ├── LoginRequest.java
│   ├── ProyectoDTO.java
│   └── TrabajadorDTO.java
├── entity/
│   ├── Cargo.java (enum)
│   ├── EstadoProyecto.java (enum)
│   ├── Proyecto.java
│   ├── Trabajador.java
│   └── Usuario.java
├── repository/
│   ├── ProyectoRepository.java
│   ├── TrabajadorRepository.java
│   └── UsuarioRepository.java
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtService.java
├── service/
│   ├── AuthService.java
│   ├── ProyectoService.java
│   └── TrabajadorService.java
└── service/impl/
    ├── AuthServiceImpl.java
    ├── ProyectoServiceImpl.java
    └── TrabajadorServiceImpl.java
```

## 🔒 Seguridad

- Todos los endpoints excepto `/api/auth/**` requieren autenticación
- Los tokens JWT expiran en 24 horas (configurable)
- Las contraseñas se almacenan encriptadas con BCrypt
- Solo usuarios con ROLE_ADMIN pueden acceder a los endpoints

## �️ Eliminado Lógico (Soft Delete)

**¿Por qué eliminado lógico?**

En el mundo laboral, eliminar registros de forma permanente es una **mala práctica** porque:
- ❌ Se pierden datos históricos importantes
- ❌ No hay forma de recuperar información eliminada por error
- ❌ Rompe la trazabilidad y auditoría
- ❌ Puede causar problemas de integridad referencial

**Implementación:**

Todas las entidades (Usuario, Trabajador, Proyecto) tienen un campo `estadoRegistro`:
- **ACTIVO** - Registro visible y funcional
- **INACTIVO** - Registro "eliminado" (soft delete)

**Comportamiento:**

```bash
# Al eliminar un trabajador
DELETE /api/trabajadores/1

# El registro NO se elimina de la base de datos
# Solo cambia su estado: estadoRegistro = INACTIVO

# Las consultas normales solo devuelven registros ACTIVOS
GET /api/trabajadores  # Solo muestra trabajadores con estadoRegistro = ACTIVO
```

**Estados disponibles:**
- `ACTIVO` - Registro activo y visible
- `INACTIVO` - Registro eliminado lógicamente (no visible en consultas normales)

**Beneficios:**
- ✅ Recuperación de datos eliminados por error
- ✅ Mantiene historial completo
- ✅ Permite auditorías y reportes históricos
- ✅ No rompe relaciones entre entidades

## 📝 Notas

- Las tablas se crean automáticamente con `spring.jpa.hibernate.ddl-auto=update`
- Los logs SQL están habilitados para desarrollo
- El proyecto usa Lombok para reducir código boilerplate
- Relación bidireccional entre Trabajador y Proyecto (OneToMany/ManyToOne)
- **Eliminado lógico implementado** - Los registros nunca se eliminan permanentemente

## 🐛 Solución de Problemas

**Error de conexión a MySQL:**
- Verificar que MySQL esté corriendo
- Verificar credenciales en application.properties
- Verificar que la base de datos "Developers" exista

**Error 403 Forbidden:**
- Verificar que el token JWT esté incluido en el header Authorization
- Verificar que el token no haya expirado

**Error al compilar:**
- Ejecutar `mvn clean install` para actualizar dependencias
- Verificar que Lombok esté configurado en tu IDE
