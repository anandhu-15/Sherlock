FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Pre-download dependencies and compile
RUN mvn clean package -DskipTests

ENV PORT=8080
EXPOSE 8080

# Start Jetty on Render's dynamic port
CMD ["sh", "-c", "mvn jetty:run -Djetty.port=${PORT}"]
