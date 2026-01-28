# Stage 1: Build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /workspace/target/quarkus-app/quarkus-run.jar .
CMD ["java", "-jar", "quarkus-run.jar"]
