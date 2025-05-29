FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/authorizationBot-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authorizationBot-0.0.1-SNAPSHOT.jar"]
