FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app

COPY target/deal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]