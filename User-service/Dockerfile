
# Dockerfile para User Service
# user-service/Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/user-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]