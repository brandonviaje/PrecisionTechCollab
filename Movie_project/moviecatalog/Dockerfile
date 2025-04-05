# Build with Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only POM first
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests
#  Runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Optimized JVM flags for containers
ENTRYPOINT ["java", "-jar", "/app/app.jar"]