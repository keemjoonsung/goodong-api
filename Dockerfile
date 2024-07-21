FROM openjdk:17-slim

COPY build/libs/goodong-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app.jar"]