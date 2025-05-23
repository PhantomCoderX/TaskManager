###########################
# 1-й этап: сборка Maven  #
###########################
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Сначала копируем только pom.xml для кэширования зависимостей
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Затем копируем исходники и собираем
COPY src ./src
RUN mvn -B package -DskipTests

###########################
# 2-й этап: лёгкий runtime #
###########################
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
