FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/stats-server-1.0-SNAPSHOT.jar /app/stats-server.jar
EXPOSE 8080
CMD ["java", "-jar", "stats-server.jar"]