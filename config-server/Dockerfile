# Utilizamos la imagen base oficial de OpenJDK 17 en su versión slim para minimizar el tamaño
FROM openjdk:17-jdk-slim

# Creamos un usuario y grupo específico para la aplicación por seguridad
RUN groupadd -r spring && useradd -r -g spring spring

# Instalamos herramientas necesarias para monitoreo y diagnóstico
# Limpiamos la caché después para reducir el tamaño de la imagen
RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR de la aplicación
COPY target/config-server-0.0.1-SNAPSHOT.jar app.jar

# Asignamos la propiedad al usuario no root para mayor seguridad
RUN chown spring:spring /app/app.jar

# Cambiamos al usuario no root
USER spring

# Exponemos el puerto del Config Server
EXPOSE ${SERVER_PORT_CONFIG_SERVER:-8888}

# Configuramos el health check para monitoreo de la aplicación
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:${SERVER_PORT_CONFIG_SERVER:-8888}/actuator/health || exit 1

# Configuración optimizada de la JVM para microservicios
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -XX:+UseContainerSupport \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Dserver.port=${SERVER_PORT_CONFIG_SERVER:-8888} \
    -Dspring.cloud.config.server.git.timeout=5 \
    -Dspring.cloud.config.server.git.cloneOnStart=false"

# Iniciamos la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]