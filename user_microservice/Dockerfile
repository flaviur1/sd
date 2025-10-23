FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /user_microservice/pom.xml
WORKDIR /user_microservice
RUN mvn dependency:go-offline
COPY ./src/ /user_microservice/src
WORKDIR /user_microservice
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /user_microservice
COPY --from=build /user_microservice/target/user_microservice-0.0.1-SNAPSHOT.jar /user_microservice/user_microservice.jar
EXPOSE 8080
CMD ["java", "-jar", "user_microservice.jar"]
