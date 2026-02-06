# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:93b11aaaee8e000325a4cd2e3eb2dba72b855f3775a0b39628ae4590a8807a99 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:25.0.2_10-jre-noble@sha256:ae5c0c9b60992045a3a4d07c02fc4fa65c783605fafe050d110498af0fb65a75
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
