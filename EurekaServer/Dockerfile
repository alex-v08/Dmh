FROM openjdk:17
ARG JAR_FILE=target/EurekaServer-0.0.1-SNAPSHOT.jar.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8761