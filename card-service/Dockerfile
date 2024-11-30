
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/card-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]