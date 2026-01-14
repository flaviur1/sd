FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /monitoring_microservice/pom.xml
WORKDIR /monitoring_microservice
RUN mvn dependency:go-offline
COPY ./src/ /monitoring_microservice/src
WORKDIR /monitoring_microservice
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /monitoring_microservice
COPY --from=build /monitoring_microservice/target/monitoring_microservice-0.0.1-SNAPSHOT.jar /monitoring_microservice/monitoring_microservice.jar
EXPOSE 8080
CMD ["java", "-jar", "monitoring_microservice.jar"]
