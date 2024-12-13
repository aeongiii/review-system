FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/review-system-0.0.1-SNAPSHOT.jar /app/review-system-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/review-system-0.0.1-SNAPSHOT.jar"]