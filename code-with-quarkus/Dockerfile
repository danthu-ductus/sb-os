# Stage 1: Build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
# Copy Maven wrapper files  
COPY mvnw .  
COPY mvnw.cmd .  
COPY .mvn .mvn  
 
# Now copy pom.xml and source code (optional, but efficient for caching)  
COPY pom.xml .  
COPY src src  
 
# Run the build  
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests -Dquarkus.package.type=fast-jar

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
# Copy fast-jar to runtime
COPY --from=build /workspace/target/quarkus-app/quarkus-run.jar .
COPY --from=build /workspace/target/quarkus-app/lib ./lib
CMD ["java", "-jar", "quarkus-run.jar"]

