#!/bin/bash

echo "🚀 Iniciando build del proyecto RestLosPinos..."

# Limpiar y compilar el proyecto
echo "📦 Compilando con Maven..."
mvn clean package -DskipTests

# Verificar que el JAR se creó correctamente
if [ -f "target/RestLosPinos-1.0-SNAPSHOT.jar" ]; then
    echo "✅ Build exitoso! JAR creado en target/RestLosPinos-1.0-SNAPSHOT.jar"
    echo "📊 Tamaño del JAR: $(du -h target/RestLosPinos-1.0-SNAPSHOT.jar | cut -f1)"
else
    echo "❌ Error: No se pudo crear el archivo JAR"
    exit 1
fi

# Construir imagen Docker (opcional)
if command -v docker &> /dev/null; then
    echo "🐳 Construyendo imagen Docker..."
    docker build -t vallegrande-backend .
    echo "✅ Imagen Docker construida exitosamente"
else
    echo "⚠️  Docker no está instalado. Omitiendo construcción de imagen."
fi

echo "🎉 Build completado! El proyecto está listo para desplegar en Render." 