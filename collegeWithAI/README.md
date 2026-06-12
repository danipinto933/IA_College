# College With AI (Backend)

Este es el backend de un sistema de gestión universitaria inteligente desarrollado con **Java 21**, **Spring Boot 3.2.5** y **Spring AI**. Expone una API REST que se comunica con una aplicación frontend en React.

## 📌 ¿De qué va el proyecto?

El propósito del sistema es gestionar la información académica (Departmentos, Asignaturas, Profesores, Estudiantes y Aulas) pero elevando la experiencia mediante la integración de inteligencia artificial.

La IA (configurada para usar modelos de OpenAI a través de OpenRouter) actúa como un **asistente administrativo integrado**. Mediante *Function Calling*, la IA tiene la capacidad de consultar y gestionar las entidades de la base de datos directamente a través del chat. 

Para prevenir "alucinaciones" de la IA o modificaciones accidentales, la aplicación implementa una estricta política de seguridad conocida como **Human-in-the-Loop** (Humano en el Bucle):
* **Operaciones de Lectura (Consultas):** La IA puede buscar y listar datos de forma automática.
* **Operaciones de Escritura (Crear, Actualizar, Borrar):** Tienen un flujo de confirmación de dos pasos donde la IA requiere explícitamente el permiso del usuario antes de ejecutar los cambios en la base de datos.

Además, el backend incluye un `DataSeeder` inteligente que automatiza la creación de datos semilla para el sistema (distribuyendo estudiantes, clases y departmentos con relaciones realistas).

---

## 🛠️ Requisitos Previos

Para ejecutar este proyecto en tu entorno local, necesitas tener instalado:
* **Java Development Kit (JDK) 21**
* **PostgreSQL** ejecutándose localmente.

*(No es necesario instalar Maven manualmente, ya que el proyecto incluye el wrapper `mvnw` / `mvnw.cmd`).*

---

## 🚀 Cómo arrancar el backend

Sigue estos pasos para poner la aplicación en marcha:

### 1. Configurar las variables de entorno
El proyecto utiliza la librería `spring-dotenv` para cargar las credenciales de la base de datos y evitar que queden expuestas. Debes crear un archivo llamado `.env` en el directorio raíz del proyecto (al mismo nivel que el archivo `pom.xml`) con el siguiente contenido:

```env
DB_URL=jdbc:postgresql://localhost:5432/postgres
DB_USERNAME=tu_usuario_de_postgres
DB_PASSWORD=tu_contraseña_de_postgres
```

O bien puedes escribirlo directamente en `application.properties`:

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=tu_usuario_de_postgres
spring.datasource.password=tu_contraseña_de_postgres

*Importante: Asegúrate de que la base de datos mencionada en la URL (por ejemplo `postgres`) ya exista en tu servidor PostgreSQL.*

### 2. Configurar la clave de la IA (Opcional)
La configuración de la inteligencia artificial se encuentra en `src/main/resources/application.properties`. Actualmente utiliza la API de OpenRouter con el modelo `gpt-4o-mini`. 
Si la clave proveída (`spring.ai.openai.api-key`) llegase a expirar, deberás sustituirla por una nueva en ese mismo archivo.

### 3. Ejecutar la aplicación

Puedes ejecutar la aplicación directamente desde tu entorno de desarrollo (IDE) como IntelliJ IDEA, Eclipse o VS Code, arrancando la clase principal `CollegeWithAiApplication.java`.

Alternativamente, puedes ejecutar el proyecto desde la terminal usando el wrapper de Maven:

**En Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**En Linux / macOS:**
```bash
./mvnw spring-boot:run
```

### 4. Clonar el frontend (Opcional pero recomendado)

Para poder probar los servicios del proyecto con una interfaz visual y una experiencia de usuario completa, puedes clonar y ejecutar el repositorio del frontend personalizado, desarrollado en React:

```bash
git clone https://github.com/47168143l/collegeWithAI_frontend.git
```

Una vez clonado, sigue las instrucciones en el `README.md` de ese repositorio para instalar las dependencias y arrancarlo de manera local.

---

## ⚠️ Notas importantes

- **Reinicio de la base de datos (`create-drop`):** En `application.properties`, la propiedad `spring.jpa.hibernate.ddl-auto` está establecida en `create-drop`. Esto significa que cada vez que apagues y enciendas el servidor, la base de datos **se borrará y se volverá a crear**. Si quieres que los datos persistan, cambia el valor de la propiedad a `update`.
- **Autollenado de datos:** Gracias al reseteo anterior y al uso del `DataSeeder`, cada vez que arranques el servidor tendrás un entorno limpio pero poblado de datos aleatorios y estructurados, listos para hacer pruebas inmediatamente. Si deseas que los datos persistan, cambia el valor de la propiedad a `update`.
