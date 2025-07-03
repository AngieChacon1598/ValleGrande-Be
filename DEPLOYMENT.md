# Guía de Despliegue en Render

## Configuración del Proyecto

Este proyecto Spring Boot está configurado para desplegarse en Render.com usando Docker.

### Archivos de Configuración

- `Dockerfile`: Configuración de Docker para containerizar la aplicación
- `render.yaml`: Configuración de despliegue para Render
- `application.yml`: Configuración de Spring Boot con perfiles
- `application-production.yml`: Configuración específica para producción

### Variables de Entorno Requeridas

En Render, necesitas configurar las siguientes variables de entorno:

1. **DB_USERNAME**: Usuario de la base de datos Oracle
2. **DB_PASSWORD**: Contraseña de la base de datos Oracle
3. **ACTUATOR_PASSWORD**: Contraseña para el endpoint de health check (opcional)
4. **TNS_ADMIN**: Ruta al directorio Wallet de Oracle (se configura automáticamente)

### Pasos para Desplegar

1. **Conectar el repositorio a Render**:
   - Ve a [render.com](https://render.com)
   - Crea una nueva cuenta o inicia sesión
   - Haz clic en "New +" y selecciona "Web Service"
   - Conecta tu repositorio de GitHub/GitLab

2. **Configurar el servicio**:
   - **Name**: `vallegrande-backend`
   - **Environment**: `Docker`
   - **Region**: Elige la más cercana a tus usuarios
   - **Branch**: `main` (o tu rama principal)
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/RestLosPinos-1.0-SNAPSHOT.jar`

3. **Configurar variables de entorno**:
   - `SPRING_PROFILES_ACTIVE`: `production`
   - `DB_USERNAME`: Tu usuario de Oracle
   - `DB_PASSWORD`: Tu contraseña de Oracle
   - `ACTUATOR_PASSWORD`: Contraseña para health check (opcional)

4. **Desplegar**:
   - Haz clic en "Create Web Service"
   - Render comenzará el proceso de build y despliegue

### Verificación del Despliegue

1. **Health Check**: `https://tu-app.onrender.com/actuator/health`
2. **API Documentation**: `https://tu-app.onrender.com/swagger-ui.html`
3. **OpenAPI**: `https://tu-app.onrender.com/v3/api-docs`

### Troubleshooting

- **Error de conexión a Oracle**: Verifica que las credenciales de la base de datos sean correctas
- **Error de puerto**: Asegúrate de que la aplicación use la variable `PORT` de Render
- **Error de Wallet**: Verifica que la carpeta `Wallet_SistemaGestionVentas` esté incluida en el repositorio

### Notas Importantes

- La aplicación usa Oracle Database, asegúrate de que sea accesible desde Render
- El plan gratuito de Render tiene limitaciones de recursos
- Los logs están disponibles en el dashboard de Render
- La aplicación se reiniciará automáticamente si se detecta un problema 