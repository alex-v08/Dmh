FROM openjdk:17-jdk-slim

# Configuración de seguridad con usuario no root
RUN groupadd -r spring && useradd -r -g spring spring

# Instalación de herramientas esenciales
RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/EurekaServer-0.0.1-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto estándar de Eureka Server
EXPOSE 8761

# Health check específico para Eureka
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:8761/actuator/health || exit 1

# Configuración optimizada para Eureka Server
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -Deureka.client.registerWithEureka=false \
    -Deureka.client.fetchRegistry=false \
    -Deureka.server.enable-self-preservation=false \
    -Deureka.server.eviction-interval-timer-in-ms=5000"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]