# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim as build

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim as slim

WORKDIR /app
RUN mkdir /app/target
COPY --from=build "/app/target/starlingbank-0.0.1-SNAPSHOT.jar" /app/target

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/starlingbank-0.0.1-SNAPSHOT.jar"]
