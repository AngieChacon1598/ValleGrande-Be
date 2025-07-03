# Guía para Oracle Database Autonomous en Render

## ✅ Compatibilidad Confirmada

Oracle Database Autonomous es **100% compatible** con tu proyecto Spring Boot. De hecho, es una excelente opción para producción.

## 🔧 Configuración Específica

### 1. **Wallet de Conexión**
Tu proyecto ya tiene configurado el wallet `Wallet_SistemaGestionVentas`. Asegúrate de que:
- ✅ La carpeta `Wallet_SistemaGestionVentas` esté en el repositorio
- ✅ Los archivos `tnsnames.ora`, `sqlnet.ora`, `cwallet.sso` estén presentes
- ✅ Las credenciales en `ojdbc.properties` sean correctas

### 2. **Variables de Entorno en Render**

Configura estas variables en el dashboard de Render:

```bash
SPRING_PROFILES_ACTIVE=oracle-autonomous
DB_USERNAME=DEVELOPER2
DB_PASSWORD=tu_contraseña_segura
TNS_ADMIN=/opt/render/project/src/Wallet_SistemaGestionVentas
```

### 3. **Configuración de Oracle Autonomous**

#### **Ventajas de Oracle Autonomous:**
- 🔒 **Seguridad automática**: Parches y actualizaciones automáticas
- 📈 **Escalabilidad**: Se adapta automáticamente a la carga
- 🛡️ **Backup automático**: Respaldos automáticos cada hora
- 🔄 **Alta disponibilidad**: 99.95% de uptime garantizado

#### **Configuración de Red:**
- **Access Control List (ACL)**: Asegúrate de que Render pueda conectarse
- **VCN**: Configura la red virtual si es necesario
- **Security Lists**: Permite conexiones desde cualquier IP (0.0.0.0/0)

## 🚀 Pasos de Despliegue

### **Paso 1: Verificar Wallet**
```bash
# Verifica que el wallet esté presente
ls -la Wallet_SistemaGestionVentas/
```

### **Paso 2: Configurar Render**
1. Ve a [render.com](https://render.com)
2. Crea un nuevo **Web Service**
3. Conecta tu repositorio
4. Configura:
   - **Environment**: `Docker`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/RestLosPinos-1.0-SNAPSHOT.jar`

### **Paso 3: Variables de Entorno**
En el dashboard de Render, agrega:
- `SPRING_PROFILES_ACTIVE`: `oracle-autonomous`
- `DB_USERNAME`: `DEVELOPER2`
- `DB_PASSWORD`: `tu_contraseña` (configurar como secreto)
- `TNS_ADMIN`: `/opt/render/project/src/Wallet_SistemaGestionVentas`

### **Paso 4: Desplegar**
Haz clic en "Create Web Service" y espera el despliegue.

## 🔍 Verificación

### **Health Check**
```bash
curl https://tu-app.onrender.com/actuator/health
```

### **Conexión a Base de Datos**
```bash
curl https://tu-app.onrender.com/actuator/health/db
```

### **Logs de Render**
Revisa los logs en el dashboard de Render para verificar la conexión.

## ⚠️ Consideraciones Importantes

### **1. Seguridad**
- ✅ Usa credenciales seguras
- ✅ Configura `DB_PASSWORD` como variable secreta en Render
- ✅ No subas credenciales al repositorio

### **2. Rendimiento**
- ✅ Oracle Autonomous se auto-escala
- ✅ El pool de conexiones está optimizado (máximo 5 conexiones)
- ✅ Timeouts configurados para evitar bloqueos

### **3. Costos**
- 💰 Oracle Autonomous tiene un costo por hora
- 💰 El plan gratuito de Render tiene limitaciones
- 💰 Considera el plan de pago para mejor rendimiento

## 🛠️ Troubleshooting

### **Error: "ORA-12541: TNS:no listener"**
- Verifica que el wallet esté correcto
- Confirma que `TNS_ADMIN` apunte al directorio correcto
- Revisa que `tnsnames.ora` tenga la configuración correcta

### **Error: "ORA-01017: invalid username/password"**
- Verifica las credenciales en Render
- Confirma que el usuario tenga permisos en Oracle Autonomous

### **Error: "Connection timeout"**
- Verifica la configuración de red en Oracle Autonomous
- Confirma que Render pueda conectarse a Oracle

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs en Render
2. Verifica la configuración de Oracle Autonomous
3. Confirma que el wallet esté actualizado
4. Contacta al soporte de Oracle si es necesario

## 🎯 Beneficios de Oracle Autonomous + Render

- **Escalabilidad automática**: Se adapta a la demanda
- **Seguridad empresarial**: Patches automáticos
- **Alta disponibilidad**: 99.95% uptime
- **Backup automático**: Sin pérdida de datos
- **Despliegue rápido**: Render + Docker
- **Monitoreo**: Health checks automáticos 