# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Only copy pom.xml first to download dependencies and cache them
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -B -DskipTests

# Stage 2: Create execution container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Expose default Spring Boot port
EXPOSE 8080

# Environment variables for database configuration
# Can be overridden by docker-compose, CI or k8s
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/jakartaee
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password

# Copy JAR from the build stage
COPY --from=build /app/target/tp-jakarta-1.0-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
