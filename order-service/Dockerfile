FROM openjdk:17

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} oderservice.jar
ENTRYPOINT [ "java", "-jar", "/oderservice.jar" ]   
EXPOSE 8080