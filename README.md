# 📌 Sistema de Reservas - Spring Boot + React

Sistema completo de gestión de reservas desarrollado con tecnologías modernas para entornos empresariales.

## 🏢 Enfoque Empresarial

Este sistema está especialmente diseñado para empresas de servicios que requieren gestión eficiente de citas y reservas, tales como clínicas médicas, centros de belleza, spa, estudios de tatuajes, consultorios profesionales, talleres especializados y centros de capacitación. La arquitectura modular permite adaptarse fácilmente a diferentes tipos de negocios que manejan servicios por citas, ofreciendo control granular de usuarios con roles diferenciados (administradores, supervisores y empleados), gestión de servicios con precios en moneda local, y un sistema de notificaciones automatizado que mejora la comunicación con los clientes y optimiza la operación diaria del negocio.

## 🚀 Tecnologías Utilizadas

### **Backend - Spring Boot**
- **Spring Boot 3.2.0** - Framework principal para desarrollo de aplicaciones Java
- **Spring Web** - Desarrollo de APIs REST para comunicación con el frontend
- **Spring Data JPA** - Mapeo objeto-relacional y persistencia de datos automática
- **Spring Security** - Autenticación, autorización y gestión de sesiones seguras
- **Hibernate** - ORM (Object-Relational Mapping) para manejo automático de base de datos
- **Maven** - Gestión de dependencias y construcción del proyecto
- **Java 17+** - Lenguaje de programación backend

### **Frontend - React**
- **React 18** - Biblioteca principal para interfaces de usuario interactivas
- **React Router** - Navegación y enrutamiento de páginas
- **React Hook Form** - Gestión eficiente de formularios y validaciones
- **Axios** - Cliente HTTP para consumir APIs REST del backend
- **TailwindCSS** - Framework de estilos para diseño moderno y responsive
- **React Hot Toast** - Notificaciones elegantes para feedback del usuario
- **Heroicons** - Iconografía profesional y consistente

### **Base de Datos**
- **PostgreSQL** - Base de datos relacional robusta y escalable
- **Migraciones automáticas** - Hibernate gestiona la estructura de BD automáticamente

### **Arquitectura y Patrones**
- **API REST** - Comunicación estandarizada entre frontend y backend
- **DTO (Data Transfer Objects)** - Objetos optimizados para transferencia de datos
- **Repository Pattern** - Abstracción del acceso a datos
- **Service Layer** - Lógica de negocio separada de la presentación
- **CORS habilitado** - Comunicación segura entre diferentes puertos

## 🏗️ Arquitectura del Sistema

### **Separación de Responsabilidades**

**Backend (Puerto 8080)**
- Servidor embebido Tomcat integrado en Spring Boot
- APIs REST para todas las operaciones (CRUD completo)
- Autenticación JWT para sesiones seguras
- Validación automática de datos con Bean Validation
- Manejo centralizado de errores y excepciones

**Frontend (Puerto 3000)**
- Aplicación React de una sola página (SPA)
- Componentes reutilizables y modulares
- Estado global con Context API
- Rutas protegidas según roles de usuario
- Diseño responsive para cualquier dispositivo

**Base de Datos**
- Esquema relacional normalizado
- Relaciones automáticas entre entidades
- Índices optimizados para consultas rápidas

## 🎯 Funcionalidades Principales

### **Gestión de Reservas**
- Creación de reservas con validación completa
- Listado y filtrado por estado (Pendiente, Confirmada, Rechazada)
- Confirmación y rechazo por parte del administrador

### **Administración de Servicios**
- CRUD completo de servicios (Crear, Leer, Actualizar, Eliminar)
- Gestión de precios sin decimales
- Control de duración y disponibilidad de servicios
- Activación/desactivación de servicios

### **Gestión de Usuarios**
- Sistema de roles (Administrador, Supervisor, Empleado, Cliente)
- Gestión de permisos granular por módulos
- Dashboard personalizado según permisos
- Gestión de usuarios y departamentos

### **Panel Administrativo**
- Dashboard con estadísticas en tiempo real
- Gestión de clientes y historial de reservas
- Reportes visuales con métricas de rendimiento
- Configuración del sistema personalizable

### **Seguridad y Autenticación**
- Login seguro con Spring Security
- Rutas protegidas según roles de usuario
- Validación de tokens JWT automática
- Sistema de permisos granular por módulos

## 📋 Prerrequisitos

- **Java 17+** - JDK instalado y configurado
- **Node.js 16+** - Runtime para el frontend
- **PostgreSQL 12+** - Base de datos
- **Maven** - Gestor de dependencias (incluido en el proyecto)

## 🔧 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/xOkimurax/Reservas.git
cd Reservas
```

### 2. Base de Datos

```bash
# Crear base de datos en PostgreSQL
psql -U postgres
CREATE DATABASE reservas_db;
\q

# Ejecutar el script de creación de tablas
psql -U postgres -d reservas_db -f database/schema.sql
```

### 3. Backend (Spring Boot)

```bash
# Navegar al directorio backend
cd backend

# Configurar base de datos en src/main/resources/application.properties
# Ajustar credenciales de PostgreSQL:
# spring.datasource.username=tu_usuario
# spring.datasource.password=tu_password

# Ejecutar la aplicación
./mvnw spring-boot:run
# o si tienes Maven instalado globalmente:
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

### 4. Configuración de Variables de Entorno

```bash
# Backend - Copiar archivo de ejemplo y configurar
cd backend
cp .env.example .env
# Editar .env con tus credenciales de base de datos

# Frontend - Copiar archivo de ejemplo y configurar
cd ../frontend
cp .env.example .env
# Ajustar REACT_APP_API_URL si es necesario
```

### 5. Frontend (React)

```bash
# Navegar al directorio frontend (si no estás ya ahí)
cd frontend

# Instalar dependencias
npm install

# Iniciar la aplicación
npm start
```

El frontend estará disponible en: `http://localhost:3000`

## 📱 Uso del Sistema

### Para Clientes
1. Visitar `http://localhost:3000`
2. Hacer clic en "Hacer una Reserva"
3. Completar el formulario con información personal y detalles de la cita
4. Confirmar la reserva

### Para Administradores
1. Visitar `http://localhost:3000/admin`
2. Usar credenciales de administrador:
   - **Email:** admin@reservas.com
   - **Password:** password
3. Gestionar reservas desde el panel de administración
4. Confirmar/Rechazar reservas
5. Gestionar usuarios y servicios

## 🔐 Credenciales por Defecto

El sistema incluye un usuario administrador por defecto:
- **Email:** admin@reservas.com
- **Password:** password

## 📊 API Endpoints Principales

### **Autenticación**
- `POST /api/auth/login` - Inicio de sesión
- `POST /api/auth/validate` - Validación de token

### **Reservas**
- `GET /api/reservas` - Listar reservas
- `POST /api/reservas` - Crear nueva reserva
- `PUT /api/reservas/{id}/confirmar` - Confirmar reserva
- `PUT /api/reservas/{id}/rechazar` - Rechazar reserva

### **Servicios**
- `GET /api/servicios` - Listar servicios
- `POST /api/servicios` - Crear servicio
- `PUT /api/servicios/{id}` - Actualizar servicio
- `DELETE /api/servicios/{id}` - Eliminar servicio

### **Usuarios**
- `GET /api/usuarios` - Listar usuarios
- `POST /api/usuarios` - Crear usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Desactivar usuario


## 🗂 Estructura del Proyecto

```
Reservas/
├── backend/                 # Spring Boot API
│   ├── src/main/java/
│   │   └── com/reservas/
│   │       ├── entity/      # Entidades JPA
│   │       ├── repository/  # Repositorios Spring Data
│   │       ├── service/     # Lógica de negocio
│   │       ├── controller/  # Controladores REST
│   │       ├── dto/         # Data Transfer Objects
│   │       └── config/      # Configuraciones
│   └── pom.xml              # Dependencias Maven
├── frontend/                # React App
│   ├── src/
│   │   ├── components/      # Componentes reutilizables
│   │   ├── pages/          # Páginas principales
│   │   ├── services/       # API calls con Axios
│   │   └── context/        # Context providers
│   └── package.json        # Dependencias NPM
├── database/
│   └── schema.sql          # Script de creación de BD
├── .gitignore              # Archivos ignorados por Git
├── LICENSE                 # Licencia del proyecto
└── README.md               # Documentación
```

## 🚦 Estados de Reserva

- **Pendiente:** Reserva creada, esperando confirmación
- **Confirmada:** Reserva aprobada por el administrador
- **Rechazada:** Reserva rechazada
- **Finalizada:** Servicio completado

## 🎨 Personalización

### Colores del Sistema
Los colores principales se pueden modificar en `frontend/tailwind.config.js`:

```javascript
colors: {
  primary: {
    50: '#eff6ff',
    500: '#3b82f6',
    600: '#2563eb',
    700: '#1d4ed8',
  }
}
```

### Configuración de Servicios
Los servicios se pueden gestionar desde el panel administrativo o modificando directamente en la base de datos.

## 🐛 Solución de Problemas

### Backend no inicia
- Verificar que PostgreSQL esté corriendo
- Comprobar credenciales en `application.properties`
- Asegurar que Java 17+ esté instalado: `java -version`

### Frontend no se conecta al backend
- Verificar que el backend esté corriendo en puerto 8080
- Comprobar que no hay conflictos de CORS

### Error de Base de Datos
- Verificar que la base de datos `reservas_db` existe
- Ejecutar el script `database/schema.sql`
- Comprobar conexión con: `psql -U postgres -d reservas_db`

## 🚀 Despliegue en Producción

### Backend
```bash
# Compilar JAR ejecutable
./mvnw clean package

# Ejecutar en producción
java -jar target/sistema-reservas-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
# Compilar para producción
npm run build

# Los archivos estáticos estarán en build/
```

### Servicios en la Nube
- **Backend:** Railway, Heroku, AWS EC2
- **Frontend:** Netlify, Vercel, AWS S3
- **Base de Datos:** Railway PostgreSQL, Render, Supabase

## ⚙️ Configuración de Variables de Entorno

### Archivos de Configuración

El proyecto incluye archivos `.env.example` en ambos directorios (frontend y backend) que debes copiar y configurar:

```bash
# Backend
cp backend/.env.example backend/.env

# Frontend  
cp frontend/.env.example frontend/.env
```

### Variables del Backend (`.env`)
```env
# Base de datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=reservas_db
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# JWT
JWT_SECRET=tu_clave_secreta_jwt
JWT_EXPIRATION=86400000

# Servidor
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

### Variables del Frontend (`.env`)
```env
# API del backend
REACT_APP_API_URL=http://localhost:8080/api

# Entorno
NODE_ENV=development
```

### Para Producción

#### Backend (variables de entorno del sistema)
```bash
DATABASE_URL=postgresql://usuario:password@host:puerto/basededatos
JWT_SECRET=clave_secreta_muy_segura
SERVER_PORT=8080
```

#### Frontend (`.env.production`)
```env
REACT_APP_API_URL=https://tu-backend-produccion.com/api
NODE_ENV=production
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crear branch de feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia **Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)**.

**Esto significa que:**
- ✅ Puedes ver y estudiar el código fuente
- ✅ Puedes descargar el proyecto para aprendizaje personal
- ✅ Puedes compartir el proyecto original con la debida atribución
- ❌ **NO** puedes usar el proyecto para fines comerciales
- ❌ **NO** puedes modificar o crear obras derivadas
- ❌ **NO** puedes redistribuir versiones modificadas

**Copyright © 2024 Oscar Matias Vera González. Todos los derechos reservados.**

Ver el archivo `LICENSE` para los términos completos de la licencia.