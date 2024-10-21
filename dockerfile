FROM openjdk:17-jdk-slim
WORKDIR /app

COPY searchTechTask-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh wait-for-it.sh

RUN apt-get update && apt-get install -y curl

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "elasticsearch:9200", "--", "java", "-jar", "app.jar"]