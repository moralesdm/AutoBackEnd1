FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copiar solo el JAR generado por Maven
COPY target/registration-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3001
CMD ["java", "-jar", "app.jar"]