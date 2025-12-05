# Multi-stage Dockerfile for Enlin Spring Boot application
# Build stage: uses Maven with JDK 17 to build the application
FROM maven:3.8.8-openjdk-17 AS build
WORKDIR /app

# Copy only what is needed for dependency resolution first to take advantage of Docker layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw || true
RUN ./mvnw -B -f pom.xml dependency:go-offline -DskipTests

# Copy source and build
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Run stage: lightweight JRE image
FROM eclipse-temurin:17-jre

# Create a non-root user to run the app
RUN useradd -m appuser || true
USER appuser
WORKDIR /home/appuser

# Copy the built jar from the build stage
# Adjust JAR name if your final artifact differs
COPY --from=build /app/target/enlin-0.0.1-SNAPSHOT.jar ./app.jar

# Expose application port (adjust if your app uses a different port)
EXPOSE 8080

# Allow overriding JVM options at runtime
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /home/appuser/app.jar"]

