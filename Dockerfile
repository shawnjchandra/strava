# Build stage
FROM ubuntu:latest AS build

# Set working directory
WORKDIR /build

# Install OpenJDK 21 and update package list in a single layer
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    rm -rf /var/lib/apt/lists/*

# Copy gradle files first to cache dependencies
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /build/build/libs/application-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]