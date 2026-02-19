FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY books-api-demo/pom.xml ./books-api-demo/
COPY p11-masking-spring-boot-starter/pom.xml ./p11-masking-spring-boot-starter/

RUN ./mvnw dependency:go-offline -B

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/books-api-demo/target/books-api-demo-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
