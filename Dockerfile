# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:4520e9eecb2d47b383c4da60b797397707a61cac3b78162fb2ca9219768cea00 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:25.0.1_8-jre-noble@sha256:d8dd4342b7dbb5a9c06d0499eecca86315346acc6a20026080642610344ceb2c
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
