FROM openjdk:23-ea-22-jdk-oracle

WORKDIR /app

COPY target/events-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
