# Use an OpenJDK base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY api/build/libs/api-0.0.1-SNAPSHOT.jar /app
COPY db/build/libs/db-0.0.1-SNAPSHOT-plain.jar /app
COPY api/src/main/resources /app/resources

# Expose the port that your application listens on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-0.0.1-SNAPSHOT.jar"]