# Builder stage
FROM openjdk:21-jdk-slim as builder
WORKDIR /book-web-app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} book-web.jar
RUN java -Djarmode=layertools -jar book-web.jar extract

# Final stage
FROM openjdk:21-jdk-slim
WORKDIR /book-web-app
COPY --from=builder book-web-app/dependencies/ ./
COPY --from=builder book-web-app/spring-boot-loader/ ./
COPY --from=builder book-web-app/snapshot-dependencies/ ./
COPY --from=builder book-web-app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
