FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring

RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/user-service-1.0.0-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto configurado en user-service-dev.yml
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Configuración optimizada para servicio de usuarios
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Dspring.jpa.hibernate.ddl-auto=update \
    -Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]