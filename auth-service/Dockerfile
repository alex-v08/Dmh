FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring

RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/auth-service-1.0.0-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto configurado en Auth-Service-dev.yml
EXPOSE ${SERVER_PORT:-8083}

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:${SERVER_PORT:-8083}/actuator/health || exit 1

# Configuración específica para servicio de autenticación
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.jpa.hibernate.ddl-auto=update \
    -Dspring.datasource.hikari.maximum-pool-size=10"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]