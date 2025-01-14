FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN ./gradlew build 
RUN ./gradlew bootJar --no-daemon

FROM openjdk:21-jdk-slim

EXPOSE 9000
COPY --from=build /app/build/libs/application-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]