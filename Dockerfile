FROM openjdk:21-jdk-slim

EXPOSE 8080

WORKDIR /app

COPY module-interface/build/libs/*.jar app.jar

CMD sleep 30 && java -jar -Dspring.profiles.active=docker /app/app.jar
