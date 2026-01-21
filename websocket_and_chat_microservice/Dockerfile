FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /websocket_and_chat_microservice/pom.xml
WORKDIR /websocket_and_chat_microservice
RUN mvn dependency:go-offline
COPY ./src/ /websocket_and_chat_microservice/src
WORKDIR /websocket_and_chat_microservice
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /websocket_and_chat_microservice
COPY --from=build /websocket_and_chat_microservice/target/websocket_and_chat_microservice-0.0.1-SNAPSHOT.jar /websocket_and_chat_microservice/websocket_and_chat_microservice.jar
EXPOSE 8080
CMD ["java", "-jar", "websocket_and_chat_microservice.jar"]
