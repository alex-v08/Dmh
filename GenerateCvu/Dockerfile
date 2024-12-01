FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring

RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/GenerateCvu-0.0.1-SNAPSHOT.jar app.jar
RUN chown spring:spring /app/app.jar

USER spring

# Puerto configurado en GenerateCvu-local.yml
EXPOSE 8088

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost:8088/actuator/health || exit 1

# Configuración optimizada para servicio ligero
ENV JAVA_OPTS="\
    -Xms256m \
    -Xmx256m \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]