FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring

RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/card-service-1.0.0-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto configurado en card-service-dev.yml
EXPOSE 8085

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:8085/actuator/health || exit 1

# Configuración optimizada para servicio de tarjetas
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Dspring.jpa.hibernate.ddl-auto=update \
    -Dspring.datasource.hikari.minimum-idle=5"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]