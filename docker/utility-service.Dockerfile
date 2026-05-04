
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /project
COPY utility-service/pom.xml .
RUN mvn dependency:go-offline
COPY utility-service/src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /usr/src/app
COPY --from=build /project/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
