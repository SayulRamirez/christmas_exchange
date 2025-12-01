FROM eclipse-temurin:17-jdk-alpine
LABEL authors="saúl ramírez"
ARG JAR_FILE=target/christmas_exchange-1.jar
COPY ${JAR_FILE} app_christmas_exchange.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_christmas_exchange.jar"]