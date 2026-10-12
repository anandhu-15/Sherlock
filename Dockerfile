FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/sherlock-1.0.war app.war
COPY --from=build /root/.m2 /root/.m2
COPY --from=build /app/pom.xml .
COPY --from=build /app/src ./src

ENV PORT=8080
EXPOSE 8080

CMD mvn org.eclipse.jetty:jetty-maven-plugin:9.4.56.v20240826:run -Djetty.port=${PORT}
