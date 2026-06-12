# CollegeWithAI 🎓🤖

**CollegeWithAI** es un sistema de gestión académica full-stack integrado con Inteligencia Artificial. Está diseñado para simplificar la administración de una universidad (departamentos, profesores, estudiantes, asignaturas y clases) permitiendo interactuar con el sistema tanto mediante una interfaz visual intuitiva como a través de un chat conversacional con un asistente de IA capaz de ejecutar acciones directas sobre la base de datos.

---

## 🚀 ¿Qué hace este proyecto?

El sistema simula y gestiona la administración de un campus universitario:
- **Gestión Escolar Completa (CRUD):** Permite listar, buscar, crear, actualizar y eliminar registros de:
  - **Departamentos** (ej. Ingeniería, Matemáticas).
  - **Profesores** (con datos de contacto, salario y departamento).
  - **Estudiantes** (con información de contacto y fecha de ingreso).
  - **Asignaturas** (horas totales, código interno, electiva o no).
  - **Clases** (calificaciones de alumnos, asignación de profesores e inscripción de alumnos).
- **Asistente de IA Administrativo (Spring AI):**
  - Integra un agente inteligente conectado a la base de datos mediante **Function Calling**.
  - Permite realizar preguntas sobre los datos (ej. *"¿Qué estudiantes pertenecen al departamento de Ingeniería?"* o *"¿Quién es el profesor mejor pagado?"*).
  - Puede ejecutar acciones de escritura (crear, actualizar o eliminar registros).
- **Protocolo de Seguridad de IA en 2 Pasos:**
  - Para evitar que la IA realice modificaciones no deseadas, el sistema implementa un protocolo estricto:
    1. **Paso 1 (Solicitud):** El usuario pide una acción de escritura. La IA se detiene y pregunta explícitamente por confirmación.
    2. **Paso 2 (Confirmación):** Solo si el usuario confirma de forma explícita (diciendo "Sí", "Confirmado", etc.), la IA ejecuta la función.
- **Seeding Automático de Datos:** Al arrancar por primera vez, el sistema autogenera datos iniciales realistas para que el panel de control no esté vacío y puedas probar la aplicación inmediatamente.

---

## 🛠️ Tecnologías utilizadas

El proyecto está dividido en dos partes principales (backend y frontend) orquestadas a través de Docker:

### 1. Backend (`collegeWithAI`)
- **Java 21** & **Spring Boot 3.2.5** (Framework principal).
- **Spring AI (OpenRouter Integration):** Usado para conectar con modelos de lenguaje de última generación (por defecto `openai/gpt-4o-mini` a través de OpenRouter).
- **Spring Data JPA & Hibernate:** Para la persistencia y mapeo objeto-relacional.
- **PostgreSQL JDBC Driver:** Conexión con la base de datos.
- **Lombok:** Para reducir el código repetitivo (*boilerplate*).
- **Spring Dotenv:** Para cargar variables de entorno locales de forma segura.

### 2. Frontend (`collegeWithAi_frontend`)
- **React 19** & **Vite 8** (Construcción rápida y moderna).
- **Vanilla CSS:** Diseño premium, moderno y fluido con soporte para modo oscuro, gradientes dinámicos, micro-animaciones y diseño responsive.
- **ESLint:** Linter de código.

### 3. Base de Datos & DevOps
- **PostgreSQL 16 (Alpine):** Base de datos relacional.
- **Docker & Docker Compose:** Contenerización de los servicios en un flujo de construcción multi-etapa (*multi-stage build*), donde el frontend se compila primero y sus archivos estáticos se empaquetan dentro del JAR de Spring Boot, sirviendo la aplicación completa desde un único puerto.

---

## 🐳 Instrucciones de Despliegue con Docker

Para poner en marcha el proyecto de forma rápida y sencilla en tu ordenador usando contenedores, sigue estos pasos:

### Prerrequisitos
Debes tener instalado en tu sistema:
1. [Docker](https://www.docker.com/products/docker-desktop/)
2. [Docker Compose](https://docs.docker.com/compose/install/)

---

### Paso 1: Configurar las variables de entorno
1. En la **carpeta raíz** del proyecto, abre o crea un archivo llamado `.env` (si no existe, puedes crearlo copiando las variables del entorno).
2. Asegúrate de configurar las siguientes variables:

```env
# Credenciales de la Base de Datos (puedes dejarlas por defecto)
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Tu clave de API de OpenRouter (Requerido para la IA)
IA_API_KEY=tu_api_key_de_openrouter_aqui
```

> [!IMPORTANT]
> Debes reemplazar `tu_api_key_de_openrouter_aqui` por tu clave de API válida de OpenRouter para que el Asistente de IA funcione correctamente.

---

### Paso 2: Construir y levantar los contenedores
Abre una terminal en la carpeta raíz del proyecto y ejecuta el siguiente comando:

```bash
docker compose up --build
```
*(O `docker-compose up --build` si utilizas una versión antigua de Docker Compose).*

Este comando realizará los siguientes pasos de manera automática:
1. Descargará la imagen de **PostgreSQL 16**.
2. Iniciará una etapa con **Node.js** para compilar y empaquetar el frontend de React.
3. Copiará el bundle compilado en la carpeta de recursos estáticos del backend.
4. Iniciará una etapa con **Maven y Java 21** para compilar el backend y empaquetarlo en un archivo `.jar`.
5. Ejecutará el contenedor final con el backend y la base de datos conectados en la misma red.

---

### Paso 3: Acceder a la aplicación
Una vez que la terminal indique que los servicios han arrancado correctamente (el backend mostrará el logo de Spring Boot y el log de inicialización de Tomcat):

- Abre tu navegador web e ingresa a: **[http://localhost:8080](http://localhost:8080)**
- Podrás ver el panel de control, explorar los departamentos, estudiantes, profesores, y hacer clic en el botón de **Asistente IA** en la esquina superior para chatear con el sistema.

---

## 🛠️ Desarrollo Local (Sin Docker para el código)

Si quieres hacer cambios en caliente y desarrollar localmente, puedes usar Docker únicamente para la base de datos y correr los servidores en tu sistema operativo:

### 1. Iniciar solo la base de datos
```bash
docker compose up db -d
```

### 2. Ejecutar el Backend (Spring Boot)
1. Ve al directorio del backend:
   ```bash
   cd collegeWithAI
   ```
2. Asegúrate de tener instalado Java 21. Ejecuta el servidor:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(En Windows utiliza `mvnw.cmd spring-boot:run`)*.
3. El backend escuchará en `http://localhost:8080`.

### 3. Ejecutar el Frontend (React/Vite)
1. Abre otra terminal y ve a la carpeta del frontend:
   ```bash
   cd collegeWithAi_frontend
   ```
2. Instala las dependencias necesarias:
   ```bash
   npm install
   ```
3. Inicia el servidor de desarrollo:
   ```bash
   npm run dev
   ```
4. El frontend estará disponible por defecto en `http://localhost:5173`. Las llamadas al backend se redirigirán automáticamente.
