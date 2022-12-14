FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/spring-demo-0.0.1-SNAPSHOT.jar"]