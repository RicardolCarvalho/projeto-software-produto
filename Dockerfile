FROM eclipse-temurin:21-jdk-alpine

COPY target/produto-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]