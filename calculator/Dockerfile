## Сборка
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#WORKDIR /app
#
#COPY . .
#
#RUN mvn clean package -DskipTests -pl calculator

# Запуск приложения
FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]