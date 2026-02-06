# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:93b11aaaee8e000325a4cd2e3eb2dba72b855f3775a0b39628ae4590a8807a99 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.30_7-jre-noble@sha256:d31955a2913455291fb0059e9f8f96713da60f4838364538ed7a717fca71e2e1
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
