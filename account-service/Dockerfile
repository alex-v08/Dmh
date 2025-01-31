FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring

RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/account-service-1.0.0-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto configurado en account-service-dev.yml
EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Configuración optimizada para servicio de cuentas
ENV JAVA_OPTS="\
    -Xms512m \
    -Xmx512m \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect \
    -Dspring.jpa.show-sql=true"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]