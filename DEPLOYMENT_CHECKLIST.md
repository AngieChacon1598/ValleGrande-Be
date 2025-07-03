# ✅ Checklist de Despliegue en Render

## 📋 Antes del Despliegue

### ✅ Configuración del Proyecto
- [x] Proyecto compila correctamente (`mvn clean package`)
- [x] JAR generado en `target/RestLosPinos-1.0-SNAPSHOT.jar`
- [x] Dockerfile configurado y optimizado
- [x] render.yaml configurado
- [x] Variables de entorno definidas

### ✅ Configuración de Oracle Autonomous
- [x] Wallet `Wallet_SistemaGestionVentas` presente
- [x] Archivos `tnsnames.ora`, `sqlnet.ora`, `cwallet.sso` verificados
- [x] Configuración `application-oracle-autonomous.yml` creada
- [x] Pool de conexiones optimizado para Render

### ✅ Archivos de Configuración
- [x] `.dockerignore` configurado
- [x] `.gitignore` actualizado
- [x] Scripts de build creados
- [x] Template de variables de entorno creado

## 🚀 Pasos de Despliegue

### 1. 📤 Subir a GitHub/GitLab
```bash
git add .
git commit -m "Configuración para despliegue en Render"
git push origin main
```

### 2. 🔗 Conectar a Render
1. Ve a [render.com](https://render.com)
2. Crea cuenta o inicia sesión
3. Haz clic en "New +" → "Web Service"
4. Conecta tu repositorio de GitHub/GitLab

### 3. ⚙️ Configurar el Servicio
- **Name**: `vallegrande-backend`
- **Environment**: `Docker`
- **Region**: Elige la más cercana
- **Branch**: `main`
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -jar target/RestLosPinos-1.0-SNAPSHOT.jar`

### 4. 🔐 Variables de Entorno
Configura estas variables en Render:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `oracle-autonomous` | Perfil de Spring Boot |
| `DB_USERNAME` | `DEVELOPER2` | Usuario de Oracle |
| `DB_PASSWORD` | `tu_contraseña` | Contraseña de Oracle (secreto) |
| `TNS_ADMIN` | `/app/Wallet_SistemaGestionVentas` | Ruta al wallet |
| `PORT` | `8083` | Puerto de la aplicación |
| `JAVA_OPTS` | `-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport` | Opciones JVM |

### 5. 🚀 Desplegar
- Haz clic en "Create Web Service"
- Espera el proceso de build (5-10 minutos)
- Verifica que el despliegue sea exitoso

## 🔍 Verificación Post-Despliegue

### ✅ Health Checks
```bash
# Health check general
curl https://tu-app.onrender.com/actuator/health

# Health check de base de datos
curl https://tu-app.onrender.com/actuator/health/db

# Información de la aplicación
curl https://tu-app.onrender.com/actuator/info
```

### ✅ Endpoints de la API
```bash
# Documentación Swagger
https://tu-app.onrender.com/swagger-ui.html

# OpenAPI
https://tu-app.onrender.com/v3/api-docs

# Endpoints de ejemplo
https://tu-app.onrender.com/api/categories
https://tu-app.onrender.com/api/products
```

### ✅ Logs
- Revisa los logs en el dashboard de Render
- Verifica que no haya errores de conexión a Oracle
- Confirma que la aplicación esté funcionando correctamente

## 🛠️ Troubleshooting

### ❌ Error: "Build failed"
- Verifica que el proyecto compile localmente
- Revisa los logs de build en Render
- Confirma que todos los archivos estén en el repositorio

### ❌ Error: "Connection to Oracle failed"
- Verifica las credenciales de la base de datos
- Confirma que el wallet esté correcto
- Revisa que Oracle Autonomous permita conexiones desde Render

### ❌ Error: "Port already in use"
- Render asignará automáticamente el puerto
- Verifica que uses la variable `PORT` en la configuración

### ❌ Error: "Out of memory"
- Reduce `JAVA_OPTS` si es necesario
- Considera el plan de pago de Render

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs en Render
2. Verifica la configuración de Oracle Autonomous
3. Confirma que todas las variables de entorno estén configuradas
4. Contacta al soporte de Render si es necesario

## 🎯 URLs Finales

Una vez desplegado correctamente:
- **Aplicación**: `https://tu-app.onrender.com`
- **API Docs**: `https://tu-app.onrender.com/swagger-ui.html`
- **Health Check**: `https://tu-app.onrender.com/actuator/health`
- **OpenAPI**: `https://tu-app.onrender.com/v3/api-docs` 