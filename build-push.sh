#!/bin/bash

# Configuración
DOCKER_HUB_USERNAME="atuhome"
VERSION="latest"

# Array de servicios con nombres en minúsculas y rutas corregidas
declare -A SERVICES=(
    ["eureka-server"]="./eureka-server"
    ["config-server"]="./config-server"
    ["api-gateway"]="./api-gateway"
    ["auth-service"]="./auth-service"
    ["user-service"]="./User-service" 
    ["account-service"]="./account-service"
    ["card-service"]="./card-service"
    ["transaction-service"]="./transaction-service"
    ["generate-alias"]="./generate-alias"
    ["generate-cvu"]="./generate-cvu"
)

# Función para construir y subir una imagen
build_and_push() {
    local service_name=$1
    local service_path=$2
    local image_name="${DOCKER_HUB_USERNAME}/${service_name,,}:${VERSION}"

    echo "📦 Procesando ${service_name}..."

    # Verificar si el directorio existe
    if [ ! -d "${service_path}" ]; then
        echo "❌ Error: Directorio ${service_path} no encontrado"
        return 1
    fi

    # Verificar si existe el Dockerfile
    if [ ! -f "${service_path}/Dockerfile" ]; then
        echo "❌ Error: Dockerfile no encontrado en ${service_path}"
        return 1
    fi

    # Construir la imagen
    echo "🔨 Construyendo ${image_name}..."
    docker build -t ${image_name} ${service_path} || {
        echo "❌ Error construyendo ${service_name}"
        return 1
    }

    # Subir la imagen
    echo "⬆️ Subiendo ${image_name}..."
    docker push ${image_name} || {
        echo "❌ Error subiendo ${service_name}"
        return 1
    }

    echo "✅ ${service_name} completado"
    echo "----------------------------------------"
}

# Verificar login en Docker Hub
echo "🔐 Verificando acceso a Docker Hub..."
docker login || {
    echo "❌ Error: Por favor, inicia sesión en Docker Hub primero"
    exit 1
}

# Procesar cada servicio
for service_name in "${!SERVICES[@]}"; do
    build_and_push "$service_name" "${SERVICES[$service_name]}"
done

echo "🎉 Proceso completado!"