# ----------------------------------
# 🔨 BUILD STAGE
# ----------------------------------
FROM eclipse-temurin:21-jdk AS build

# Set working directory in the container for the build context
WORKDIR /app

# Copy Maven configuration and project source
COPY pom.xml .
COPY src ./src

# Install Maven (not included by default) and clean up to reduce image size
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Compile the application and skip tests to speed up the build
RUN mvn package -DskipTests

# ----------------------------------
# 🚀 RUNTIME STAGE
# ----------------------------------
FROM eclipse-temurin:21-jre

# Create a non-root user for security
RUN useradd -r -u 1001 -g root appuser

# Set the working directory for the application
WORKDIR /app

# Copy only the final JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Give ownership of the JAR file to the non-root user
RUN chown appuser:root app.jar

# Run the container as the non-root user
USER appuser

# Expose the port your application listens on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
