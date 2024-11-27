FROM openjdk:17
WORKDIR /app
COPY api-gateway/target/api-gateway-1.0-SNAPSHOT.jar /app/api-gateway.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]