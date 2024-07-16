FROM maven:3.8.3-openjdk-17 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -DskipTests

FROM amazoncorretto:17
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/Backend-0.0.1-SNAPSHOT.jar /usr/src/spring/
WORKDIR /usr/src/spring
EXPOSE 8080
CMD ["java","-Dspring.profiles.active=dockercompose","-jar", "Backend-0.0.1-SNAPSHOT.jar"]
