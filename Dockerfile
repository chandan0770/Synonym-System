FROM maven:3.8.3-openjdk-17

COPY . /app/

WORKDIR /app
RUN mvn package

ENTRYPOINT ["java", "-jar", "/app/target/assignment-0.0.1-SNAPSHOT.jar"]
