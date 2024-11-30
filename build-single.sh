#!/bin/bash

# Configuración
SERVICE_NAME=$1
VERSION=${2:-latest}
DOCKER_HUB_USERNAME="atuhome"

if [ -z "$SERVICE_NAME" ]; then
    echo "❌ Error: Debes especificar el nombre del servicio"
    echo "Uso: ./build-single-service.sh nombre-servicio [version]"
    exit 1
fi

IMAGE_NAME="${DOCKER_HUB_USERNAME}/${SERVICE_NAME}:${VERSION}"

# Construir
echo "🔨 Construyendo ${IMAGE_NAME}..."
docker build -t ${IMAGE_NAME} .

# Push
echo "⬆️ Subiendo ${IMAGE_NAME}..."
docker push ${IMAGE_NAME}

echo "✅ Proceso completado para ${SERVICE_NAME}"