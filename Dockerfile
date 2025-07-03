# Etapa 1: Build de la app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY Wallet_SistemaGestionVentas ./Wallet_SistemaGestionVentas
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/RestLosPinos-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/Wallet_SistemaGestionVentas ./Wallet_SistemaGestionVentas

# Instalar dependencias del sistema necesarias para Oracle
RUN apt-get update && apt-get install -y \
    libaio1 \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Crear directorio para logs
RUN mkdir -p /app/logs

# Exponer el puerto (Render asignará el puerto automáticamente)
EXPOSE 8083

# Variables de entorno para optimizar JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Comando para ejecutar la aplicación con optimizaciones
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 