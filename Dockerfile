FROM openjdk:23-ea-22-jdk-oracle

WORKDIR /app

COPY target/events-1.0.21.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
