#!/bin/bash

echo "🚀 Iniciando build para Render..."

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: No se encontró pom.xml. Asegúrate de estar en el directorio raíz del proyecto."
    exit 1
fi

# Verificar que el wallet esté presente
if [ ! -d "Wallet_SistemaGestionVentas" ]; then
    echo "❌ Error: No se encontró la carpeta Wallet_SistemaGestionVentas"
    exit 1
fi

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

# Verificar que el wallet tenga todos los archivos necesarios
echo "🔐 Verificando wallet de Oracle..."
required_files=("tnsnames.ora" "sqlnet.ora" "cwallet.sso" "ojdbc.properties")
for file in "${required_files[@]}"; do
    if [ ! -f "Wallet_SistemaGestionVentas/$file" ]; then
        echo "❌ Error: Falta archivo $file en Wallet_SistemaGestionVentas/"
        exit 1
    fi
done
echo "✅ Wallet verificado correctamente"

echo "🎉 Build completado! El proyecto está listo para desplegar en Render."
echo "📋 Próximos pasos:"
echo "   1. Subir código a GitHub/GitLab"
echo "   2. Conectar repositorio a Render"
echo "   3. Configurar variables de entorno"
echo "   4. Desplegar" 