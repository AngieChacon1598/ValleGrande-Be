# Usar OpenJDK 17 como imagen base optimizada para Render
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Instalar dependencias del sistema necesarias para Oracle
RUN apt-get update && apt-get install -y \
    libaio1 \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Copiar el archivo JAR del proyecto
COPY target/RestLosPinos-1.0-SNAPSHOT.jar app.jar

# Copiar la carpeta Wallet para la conexión a Oracle
COPY Wallet_SistemaGestionVentas/ /app/Wallet_SistemaGestionVentas/

# Crear directorio para logs
RUN mkdir -p /app/logs

# Exponer el puerto (Render asignará el puerto automáticamente)
EXPOSE 8083

# Variables de entorno para optimizar JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Comando para ejecutar la aplicación con optimizaciones
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 