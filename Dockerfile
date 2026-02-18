# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:93b11aaaee8e000325a4cd2e3eb2dba72b855f3775a0b39628ae4590a8807a99 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.30_7-jre-noble@sha256:b1e9d673735829475f372ccbaf004ba35c9ae62634e2e47255ccb9fafdb5cc52
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
