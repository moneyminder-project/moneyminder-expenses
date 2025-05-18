FROM openjdk:17
COPY target/*.jar app.jar
EXPOSE 18081
ENTRYPOINT ["java", "-jar", "/app.jar"]

