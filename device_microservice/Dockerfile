FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /device_microservice/pom.xml
WORKDIR /device_microservice
RUN mvn dependency:go-offline
COPY ./src/ /device_microservice/src
WORKDIR /device_microservice
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /device_microservice
COPY --from=build /device_microservice/target/device_microservice-0.0.1-SNAPSHOT.jar /device_microservice/device_microservice.jar
EXPOSE 8080
CMD ["java", "-jar", "device_microservice.jar"]
