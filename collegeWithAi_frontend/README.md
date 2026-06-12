# CollegeWithAI - Frontend

Este es el proyecto frontend para el sistema **CollegeWithAI**. Es una aplicación construida con **React** y **Vite** que proporciona una interfaz de usuario moderna e interactiva para visualizar y gestionar datos académicos, además de incluir un sistema de chat para comunicarse con una Inteligencia Artificial.

## ¿Qué hace este proyecto?

El frontend es la parte visual de la aplicación y se encarga de:
- Visualizar datos del sistema académico (departmentos, asignaturas, profesores, alumnos, etc.).
- Interactuar con una interfaz gráfica estilizada para consumir información mediante botones y paneles.
- Integrar la vista del asistente de IA para realizar consultas e interactuar con el sistema a través de lenguaje natural.

## 🔗 Integración con el Backend

Este proyecto **no funciona de manera independiente**. Es el cliente que recibe y envía la información desde y hacia el servidor.

Toda la lógica de negocio, acceso a bases de datos, operaciones CRUD, y la inteligencia artificial real están gestionadas por el **Backend de CollegeWithAI** (desarrollado con Java y Spring Boot). Para que este frontend muestre información y funcione correctamente, el backend debe estar en ejecución, el cual actúa como la API principal (`http://localhost:8080`).

## 🚀 Cómo iniciar el proyecto localmente

### Requisitos previos
- Tener instalado **Node.js** en el equipo.
- Tener el proyecto **Backend de CollegeWithAI** levantado y ejecutándose (para que las llamadas a la API funcionen). La direccion de GitHub para clonar el proyecto y poderlo ejecutar es: `https://github.com/47168143l/collegeWithAI`

### Pasos de instalación

1. **Abrir una terminal** y navegar al directorio principal de este proyecto (`collegeWithAi_frontend`).

2. **Instalar dependencias:**
   Ejecuta el siguiente comando para descargar todos los paquetes de Node necesarios:
   ```bash
   npm install
   ```

3. **Ejecutar el entorno de desarrollo:**
   Una vez instaladas las dependencias, arranca el servidor local con Vite:
   ```bash
   npm run dev
   ```

4. **Abrir en el navegador:**
   La terminal te indicará una URL local (normalmente `http://localhost:5173`). Abre ese enlace en tu navegador web para ver la aplicación funcionando.

## 🛠️ Tecnologías utilizadas

- **React**: Biblioteca principal de JavaScript para construir las interfaces de usuario.
- **Vite**: Herramienta rápida para la compilación y el servidor de desarrollo.
- **CSS Vanilla**: Estilos personalizados que aseguran un diseño oscuro, elegante y responsivo.
