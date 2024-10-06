FROM gradle:7.6.2-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "app.jar"]
