# starlingbank-roundup
Ozge Ataseven Ozdol

# Starling Bank Saving Goals Application

This application processes saving goals for a user's account on Starling Bank's sandbox environment.

## Prerequisites

- JDK 17
- Maven
- Docker

## Getting Started

### Build the Project

To clean and build the project, run:

```sh
mvn clean install
```

### Run Tests
```sh
mvn clean test
```

### Run the Application
```sh
mvn spring-boot:run
```
### Using the Application

Create a new customer on the Starling Bank Developer's website.

### Access the Application
The application will be accessible on your localhost at:
### POST
```sh
http://localhost:8080/saving-goals/process?minTimestamp=2024-07-30T00:00:00.000Z
```
### Call the REST API

Pass the Authorization key and minTimestamp as input to call the REST API. The application will fetch the first 7 days' transactions starting from the provided minTimestamp automatically 
and create a Saving Goal in according to transactions.

### Docker
- Build the Docker Image
To build the Docker image, use:

```ssh
docker buildx build -t starlingbank:0.0.1-SNAPSHOT -f Dockerfile .
```
- Run the Docker Container
To run the Docker container, execute:

```ssh
docker run --rm -p 8080:8080 starlingbank:0.0.1-SNAPSHOT
```
