
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/generate-alias-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
