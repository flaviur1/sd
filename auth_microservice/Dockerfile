FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /auth_microservice/pom.xml
WORKDIR /auth_microservice
RUN mvn dependency:go-offline
COPY ./src/ /auth_microservice/src
WORKDIR /auth_microservice
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /auth_microservice
COPY --from=build /auth_microservice/target/auth_microservice-0.0.1-SNAPSHOT.jar /auth_microservice/auth_microservice.jar
EXPOSE 8080
CMD ["java", "-jar", "auth_microservice.jar"]
