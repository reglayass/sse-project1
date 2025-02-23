# Use an official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /springboot

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /springboot

# Copy the JAR file from the build stage into the container
COPY --from=build /springboot/target/application-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (change if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]